
void _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEC2B8ne180000ILi0EEEPKc
               (long *param_1,char *param_2)

{
  code *pcVar1;
  size_t __n;
  void *__dest;

  __n = strlen(param_2);
  if (0xffffffffffffffef < __n) {
    std::string::__throw_length_error_abi_ne180000_();
                    /* WARNING: Does not return */
    pcVar1 = (code *)UndefinedInstructionException(0,0x220cfc);
    (*pcVar1)();
  }
  if (__n < 0x17) {
    __dest = (void *)((long)param_1 + 1);
    *(char *)param_1 = (char)((int)__n << 1);
    if (__n == 0) goto LAB_00220ce0;
  }
  else {
    __dest = operator_new((__n | 0xf) + 1);
    param_1[1] = __n;
    param_1[2] = (long)__dest;
    *param_1 = (__n | 0xf) + 2;
  }
  memmove(__dest,param_2,__n);
LAB_00220ce0:
  *(undefined1 *)((long)__dest + __n) = 0;
  return;
}
