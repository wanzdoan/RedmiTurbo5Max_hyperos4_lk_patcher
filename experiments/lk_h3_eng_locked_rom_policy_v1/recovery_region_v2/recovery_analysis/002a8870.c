
void property_get(undefined8 param_1,char *param_2,char *param_3,undefined8 param_4)

{
  int iVar1;
  size_t sVar2;
  char *pcVar3;

  pcVar3 = param_3;
  iVar1 = __system_property_get();
  if ((param_3 != (char *)0x0) && (iVar1 < 1)) {
    snprintf(param_2,0xffffffffffffffff,pcVar3,param_4,param_3);
    sVar2 = strlen(param_2);
    if (sVar2 != (long)(int)sVar2) {
                    /* WARNING: Subroutine does not return */
      __ubsan_handle_implicit_conversion_minimal_abort();
    }
  }
  return;
}
