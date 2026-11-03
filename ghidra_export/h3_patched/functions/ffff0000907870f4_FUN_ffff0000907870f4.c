// ffff0000907870f4  size=168  FUN_ffff0000907870f4

undefined8 FUN_ffff0000907870f4(undefined8 param_1,undefined8 param_2,long *param_3)

{
  long lVar1;
  long lVar2;
  char *pcVar3;
  
  lVar1 = FUN_ffff00009077d24c(param_2);
  if (lVar1 == 0) {
    pcVar3 = s__secinfo___s__Alloc_header_buffe_ffff0000907f8ccd;
  }
  else {
    lVar2 = FUN_ffff00009077bd60(s_secinfo_ffff0000907d38b9);
    if (lVar2 == 0) {
      pcVar3 = s__secinfo___s__partition_open_fai_ffff0000907e8581;
    }
    else {
      lVar2 = FUN_ffff00009077bdf4(lVar2,lVar1,param_1,param_2);
      if (0 < lVar2) {
        *param_3 = lVar1;
        return 0;
      }
      pcVar3 = s__secinfo___s__partition_read_fai_ffff0000907f5431;
    }
  }
  FUN_ffff00009077de54(pcVar3,s_ReadPartitionData_ffff0000907ffc71);
  return 0xffffffff;
}


