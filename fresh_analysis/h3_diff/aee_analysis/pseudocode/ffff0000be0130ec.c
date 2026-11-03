
int FUN_ffff0000be0130ec(undefined8 param_1)

{
  int iVar1;
  undefined1 *local_30;
  undefined1 *puStack_28;
  undefined8 local_20;
  undefined8 uStack_18;
  
  puStack_28 = &stack0xffffffffffffffb0;
  if (DAT_ffff0000be107938 != 0) {
    if (DAT_ffff0000be107940._4_4_ < (int)DAT_ffff0000be107940) {
      local_20 = 0;
      uStack_18 = 0xffffffc8;
      local_30 = (undefined1 *)register0x00000008;
      iVar1 = FUN_ffff0000be061230
                        (DAT_ffff0000be107938 + DAT_ffff0000be107940._4_4_,
                         (long)((int)DAT_ffff0000be107940 - DAT_ffff0000be107940._4_4_),param_1,
                         &local_30);
      if (-1 < iVar1) {
        DAT_ffff0000be107940._4_4_ = DAT_ffff0000be107940._4_4_ + iVar1;
        return iVar1;
      }
      FUN_ffff0000be05fd98(s_Error__invalid_wcnt__d__ffff0000be0a5735);
    }
    else {
      FUN_ffff0000be05fd98(s_Error__exceed_mrdump_string_buf__ffff0000be0ad368);
    }
  }
  return 0;
}


