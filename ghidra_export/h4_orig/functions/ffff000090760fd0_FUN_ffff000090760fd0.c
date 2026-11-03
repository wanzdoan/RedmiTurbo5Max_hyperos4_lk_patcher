// ffff000090760fd0  size=188  FUN_ffff000090760fd0

undefined8
FUN_ffff000090760fd0(undefined8 param_1,undefined8 param_2,undefined8 param_3,long param_4)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = FUN_ffff00009077c028();
  if (lVar2 == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___partition_open_fail_ffff0000907e9d49,
               s_WritePartitionInfo_ffff0000907f03a5,0x438,s_BLACKBOX_ffff0000907d9a1f);
    uVar3 = 0;
  }
  else {
    iVar1 = FUN_ffff00009077c144(lVar2,param_2,param_3,param_4);
    if (iVar1 < param_4) {
      FUN_ffff00009077e11c
                (s__s__d____s___partition_write_fai_ffff0000907dd0e6,
                 s_WritePartitionInfo_ffff0000907f03a5,0x43e,s_BLACKBOX_ffff0000907d9a1f);
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


