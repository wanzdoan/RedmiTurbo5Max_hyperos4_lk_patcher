// ffff000090760c7c  size=188  FUN_ffff000090760c7c

undefined8
FUN_ffff000090760c7c(undefined8 param_1,undefined8 param_2,undefined8 param_3,long param_4)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = FUN_ffff00009077bd60();
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___partition_open_fail_ffff0000907e9c05,
               s_GetPartitionInfo_ffff0000907ecd68,0x421,s_BLACKBOX_ffff0000907d9988);
    uVar3 = 0;
  }
  else {
    iVar1 = FUN_ffff00009077bdf4(lVar2,param_2,param_3,param_4);
    if (iVar1 < param_4) {
      FUN_ffff00009077de54
                (s__s__d____s___partition_read_fail_ffff0000907f6dd1,
                 s_GetPartitionInfo_ffff0000907ecd68,0x427,s_BLACKBOX_ffff0000907d9988);
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


