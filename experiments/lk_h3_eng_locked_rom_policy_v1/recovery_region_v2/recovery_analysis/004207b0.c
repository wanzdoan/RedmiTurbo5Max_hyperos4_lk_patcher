
/* WARNING: Unknown calling convention -- yet parameter storage is locked */

int setjmp(__jmp_buf_tag *__env)

{
  int iVar1;

  iVar1 = (*(code *)PTR_setjmp_0042ab60)((int)__env);
  return iVar1;
}
