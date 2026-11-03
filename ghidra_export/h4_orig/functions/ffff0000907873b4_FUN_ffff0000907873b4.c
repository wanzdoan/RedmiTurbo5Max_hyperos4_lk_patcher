// ffff0000907873b4  size=168  FUN_ffff0000907873b4

undefined8 FUN_ffff0000907873b4(undefined8 param_1,undefined8 param_2,long *param_3)

{
  long lVar1;
  long lVar2;
  char *pcVar3;
  
  lVar1 = FUN_ffff00009077d514(param_2);
  if (lVar1 == 0) {
    pcVar3 = s__secinfo___s__Alloc_header_buffe_ffff0000907f8e69;
  }
  else {
    lVar2 = FUN_ffff00009077c028(s_secinfo_ffff0000907d38f6);
    if (lVar2 == 0) {
      pcVar3 = s__secinfo___s__partition_open_fai_ffff0000907e86dc;
    }
    else {
      lVar2 = FUN_ffff00009077c0bc(lVar2,lVar1,param_1,param_2);
      if (0 < lVar2) {
        *param_3 = lVar1;
        return 0;
      }
      pcVar3 = s__secinfo___s__partition_read_fai_ffff0000907f55b5;
    }
  }
  FUN_ffff00009077e11c(pcVar3,s_ReadPartitionData_ffff0000907ffe19);
  return 0xffffffff;
}


