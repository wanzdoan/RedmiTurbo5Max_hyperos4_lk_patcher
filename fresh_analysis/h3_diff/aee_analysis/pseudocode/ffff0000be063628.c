
void FUN_ffff0000be063628(undefined8 param_1,undefined8 param_2,undefined4 param_3)

{
  long lVar1;
  long lVar2;
  
  lVar1 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar1 != 0) {
    lVar2 = FUN_ffff0000be05e088(lVar1,param_2,param_1,param_3);
    if (lVar2 < 0) {
      FUN_ffff0000be063b40(s_kedump__failed_to_update_panic_h_ffff0000be0b6df5);
      FUN_ffff0000be05fd98(s_kedump__failed_to_update_panic_h_ffff0000be0b6df5);
    }
    FUN_ffff0000be05def0(lVar1);
    return;
  }
  FUN_ffff0000be063b40(s_kedump__partition___s__is_not_ex_ffff0000be0a3bba,s_expdb_ffff0000be0a79cf)
  ;
  FUN_ffff0000be05fd98(s_kedump__partition___s__is_not_ex_ffff0000be0a3bba,s_expdb_ffff0000be0a79cf)
  ;
  return;
}


