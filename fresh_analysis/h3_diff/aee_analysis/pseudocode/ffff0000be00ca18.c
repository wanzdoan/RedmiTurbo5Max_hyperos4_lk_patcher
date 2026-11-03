
undefined8 FUN_ffff0000be00ca18(void)

{
  undefined8 *puVar1;
  int iVar2;
  undefined8 uVar3;
  
  puVar1 = DAT_ffff0000be0fb000;
  if (DAT_ffff0000be0fb000 == (undefined8 *)0x0) {
    FUN_ffff0000be063b40(s__s__mrdump_cb_is_NULL_ffff0000be0b089e,s__get_mpt_ffff0000be0a569f);
    FUN_ffff0000be05fd98(s__s__mrdump_cb_is_NULL_ffff0000be0b089e,s__get_mpt_ffff0000be0a569f);
    uVar3 = 0;
  }
  else {
    iVar2 = FUN_ffff0000be060778(DAT_ffff0000be0fb000,s_MRDUMP11_ffff0000be0a7a51,8);
    if ((iVar2 == 0) ||
       (iVar2 = FUN_ffff0000be060778(puVar1,s_XRDUMP_ffff0000be0a6986,6), iVar2 == 0)) {
      uVar3 = puVar1[0x12];
    }
    else {
      FUN_ffff0000be063b40
                (s_mrdump_cb__unexpected_sig_error__ffff0000be0c5f57,*puVar1,
                 s__get_mpt_ffff0000be0a569f);
      FUN_ffff0000be05fd98
                (s_mrdump_cb__unexpected_sig_error__ffff0000be0c5f57,*puVar1,
                 s__get_mpt_ffff0000be0a569f);
      uVar3 = 0;
    }
  }
  return uVar3;
}


