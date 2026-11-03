
undefined * FUN_ffff0000be014814(void)

{
  uint uVar1;
  char *pcVar2;
  long lVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  pcVar2 = (char *)FUN_ffff0000be031c40(s_MTK_DEVICE_ID_ffff0000be0c3cee);
  lVar3 = FUN_ffff0000be060e5c(&DAT_ffff0000be107a18);
  if (lVar3 == 0) {
    if (pcVar2 == (char *)0x0) {
      FUN_ffff0000be05fd98(s_Set_serial___to_default_value__ffff0000be0b752f);
      pcVar2 = s_0123456789ABCDEF_ffff0000be0c18db;
      uVar5 = FUN_ffff0000be060e5c(s_0123456789ABCDEF_ffff0000be0c18db);
      uVar5 = uVar5 & 0xffffffff;
    }
    else {
      FUN_ffff0000be05fd98(s_Set_serial___from_para__ffff0000be0ab397);
      uVar1 = FUN_ffff0000be060e5c(pcVar2);
      if (0x12 < uVar1) {
        uVar1 = 0x13;
      }
      uVar5 = (ulong)uVar1;
    }
    FUN_ffff0000be060eb8(&DAT_ffff0000be107a18,pcVar2,uVar5);
    (&DAT_ffff0000be107a18)[uVar5] = 0;
    lVar3 = thunk_FUN_ffff0000be0364a4(0xd);
    uVar5 = thunk_FUN_ffff0000be0364a4(0xc);
    uVar5 = uVar5 & 0xffffffff | lVar3 << 0x20;
    if (uVar5 == 0) {
      uVar4 = FUN_ffff0000be060e5c(s_0123456789ABCDEF_ffff0000be0c18db);
      FUN_ffff0000be0607b0(&DAT_ffff0000be107a18,s_0123456789ABCDEF_ffff0000be0c18db,uVar4);
      uVar4 = 2;
    }
    else {
      FUN_ffff0000be014624(uVar5,0x50f7f50c,&DAT_ffff0000be107a18);
      uVar4 = 1;
    }
    FUN_ffff0000be05fd98(s_Set_serial___from_efuse__error____ffff0000be0bbc5e,uVar4);
    uVar1 = FUN_ffff0000be060e5c(&DAT_ffff0000be107a18);
    if (0x12 < uVar1) {
      uVar1 = 0x13;
    }
    (&DAT_ffff0000be107a18)[uVar1] = 0;
  }
  return &DAT_ffff0000be107a18;
}


