// ffff000090760d38  size=188  FUN_ffff000090760d38

undefined8
FUN_ffff000090760d38(undefined8 param_1,undefined8 param_2,undefined8 param_3,long param_4)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = FUN_ffff00009077bd60();
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___partition_open_fail_ffff0000907e9c05,
               s_WritePartitionInfo_ffff0000907f0234,0x438,s_BLACKBOX_ffff0000907d9988);
    uVar3 = 0;
  }
  else {
    iVar1 = FUN_ffff00009077be7c(lVar2,param_2,param_3,param_4);
    if (iVar1 < param_4) {
      FUN_ffff00009077de54
                (s__s__d____s___partition_write_fai_ffff0000907dd010,
                 s_WritePartitionInfo_ffff0000907f0234,0x43e,s_BLACKBOX_ffff0000907d9988);
      FUN_ffff00009077bce4(lVar2);
      uVar3 = 0;
    }
    else {
      FUN_ffff00009077bce4(lVar2);
      uVar3 = 1;
    }
  }
  return uVar3;
}


