
undefined8
FUN_ffff0000be04cff4(undefined8 param_1,undefined8 param_2,undefined8 param_3,long param_4)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = FUN_ffff0000be05df6c();
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d____s___partition_open_fail_ffff0000be0b5a1e,
               s_GetPartitionInfo_ffff0000be0b7dc1,0x421,s_BLACKBOX_ffff0000be0aa9ee);
    uVar3 = 0;
  }
  else {
    iVar1 = FUN_ffff0000be05e000(lVar2,param_2,param_3,param_4);
    if (iVar1 < param_4) {
      FUN_ffff0000be05fd98
                (s__s__d____s___partition_read_fail_ffff0000be0beae0,
                 s_GetPartitionInfo_ffff0000be0b7dc1,0x427,s_BLACKBOX_ffff0000be0aa9ee);
      FUN_ffff0000be05def0(lVar2);
      uVar3 = 0;
    }
    else {
      FUN_ffff0000be05def0(lVar2);
      uVar3 = 1;
    }
  }
  return uVar3;
}


