
long FUN_ffff0000be011bd8(void)

{
  long lVar1;
  long lVar2;
  int iVar3;
  
  lVar2 = DAT_ffff0000be106928;
  lVar1 = DAT_ffff0000be106928 + 0x2000;
  if (DAT_ffff0000be106928 != 0 && lVar1 != 0) {
    iVar3 = FUN_ffff0000be060778(lVar1,s_MRDUMP11_ffff0000be0a7a51,8);
    if (iVar3 == 0) {
      return lVar1;
    }
    *(undefined1 *)(lVar2 + 0x2007) = 0;
    FUN_ffff0000be05fd98(s_wrong_mcb_sig__s_ffff0000be0bcd80,lVar1);
  }
  return 0;
}


