// ffff000090715568  size=128  FUN_ffff000090715568

undefined8 FUN_ffff000090715568(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  long lVar1;
  char *pcVar2;
  
  lVar1 = FUN_ffff00009077bd60(s_blackbox_ffff0000907f0c04);
  if (lVar1 == 0) {
    pcVar2 = s__blackbox__WritePartitionInfo__p_ffff0000907f5c5e;
  }
  else {
    lVar1 = FUN_ffff00009077be7c(lVar1,param_3,param_1,param_2);
    if (0 < lVar1) {
      return 0;
    }
    pcVar2 = s__blackbox__WritePartitionInfo__p_ffff0000907fcc25;
  }
  FUN_ffff00009077de54(pcVar2);
  return 1;
}


