
void FUN_ffff0000be0090f0(undefined8 param_1,int param_2)

{
  long lVar1;
  undefined4 local_40;
  undefined8 local_3c;
  undefined4 local_34;
  
  local_34 = 0;
  local_3c = 0;
  local_40 = 0x78563412;
  if (0 < param_2) {
    lVar1 = 0;
    do {
      FUN_ffff0000be029a00((long)&local_40 + lVar1);
      lVar1 = lVar1 + 4;
    } while ((int)lVar1 < param_2);
  }
  FUN_ffff0000be05fd98
            (s__s__d__Use_random_key_ffff0000be0a687e,s_mrdump_gen_ivec_ffff0000be0a9ebc,0x3c);
  FUN_ffff0000be0607b0(param_1,&local_40,(long)param_2);
  return;
}


