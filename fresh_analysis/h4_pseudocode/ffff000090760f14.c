
undefined8
FUN_ffff000090760f14(undefined8 param_1,undefined8 param_2,undefined8 param_3,long param_4)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = FUN_ffff00009077c028();
  if (lVar2 == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___partition_open_fail_ffff0000907e9d49,
               s_GetPartitionInfo_ffff0000907eceeb,0x421,s_BLACKBOX_ffff0000907d9a1f);
    uVar3 = 0;
  }
  else {
    iVar1 = FUN_ffff00009077c0bc(lVar2,param_2,param_3,param_4);
    if (iVar1 < param_4) {
      FUN_ffff00009077e11c
                (s__s__d____s___partition_read_fail_ffff0000907f6f5b,
                 s_GetPartitionInfo_ffff0000907eceeb,0x427,s_BLACKBOX_ffff0000907d9a1f);
      FUN_ffff00009077bfac(lVar2);
      uVar3 = 0;
    }
    else {
      FUN_ffff00009077bfac(lVar2);
      uVar3 = 1;
    }
  }
  return uVar3;
}


