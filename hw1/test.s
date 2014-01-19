	.file	"../../mini/test.s"
	.globl	_Main_main
_Main_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	movl	$1,%eax
	movl	%eax,-4(%ebp)
	movl	$10,%eax
	movl	%eax,-8(%ebp)
	jmp	l1
l0:
	movl	-8(%ebp),%eax
	movl	-4(%ebp),%ecx
	imull	%ecx,%eax
	movl	%eax,-4(%ebp)
	movl	$1,%eax
	movl	-8(%ebp),%ecx
	xchgl	%ecx,%eax
	subl	%ecx,%eax
	movl	%eax,-8(%ebp)
l1:
	movl	$0,%eax
	movl	-8(%ebp),%ecx
	cmpl	%eax,%ecx
	jg	l0
	subl	$12,%esp
	movl	-4(%ebp),%eax
	pushl	%eax
	call	_print
	movl	%ebp,%esp
	popl	%ebp
	ret
