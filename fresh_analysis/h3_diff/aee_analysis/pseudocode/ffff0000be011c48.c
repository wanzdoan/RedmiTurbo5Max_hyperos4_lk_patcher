
undefined1 * FUN_ffff0000be011c48(void)

{
  undefined1 *puVar1;
  long lVar2;
  int iVar3;
  char *pcVar4;
  
  lVar2 = DAT_ffff0000be106928;
  puVar1 = (undefined1 *)(DAT_ffff0000be106928 + 0x2000);
  if (DAT_ffff0000be106928 == 0 || puVar1 == (undefined1 *)0x0) {
    pcVar4 = s_mrdump_cb_is_NULL_ffff0000be0c3c20;
  }
  else {
    iVar3 = FUN_ffff0000be060778(puVar1,s_MRDUMP11_ffff0000be0a7a51,8);
    if (iVar3 == 0) {
      *puVar1 = 0x58;
      FUN_ffff0000be007308(puVar1,0x180);
      FUN_ffff0000be0113f4
                (s_Boot_record_found_at__p__02x_02x_ffff0000be0b094b,puVar1,*puVar1,
                 *(undefined1 *)(lVar2 + 0x2001));
      return puVar1;
    }
    pcVar4 = s_No_Boot_record_found_ffff0000be0b85b8;
  }
  FUN_ffff0000be0113f4(pcVar4);
  return (undefined1 *)0x0;
}


