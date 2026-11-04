
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* std::__throw_bad_optional_access[abi:ne180000]() */

void std::__throw_bad_optional_access_abi_ne180000_(void)

{
  long *plVar1;

  plVar1 = (long *)__cxa_allocate_exception(8);
  *plVar1 = (long)(PTR_vtable_00429b78 + 0x10);
                    /* WARNING: Subroutine does not return */
  __cxa_throw(plVar1,&bad_optional_access::typeinfo,PTR__bad_optional_access_00429b70);
}
