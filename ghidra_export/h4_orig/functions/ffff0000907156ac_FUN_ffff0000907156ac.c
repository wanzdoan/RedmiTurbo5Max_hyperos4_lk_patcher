// ffff0000907156ac  size=128  FUN_ffff0000907156ac

undefined8 FUN_ffff0000907156ac(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  long lVar1;
  char *pcVar2;
  
  lVar1 = FUN_ffff00009077c028(s_blackbox_ffff0000907f0d8c);
  if (lVar1 == 0) {
    pcVar2 = s__blackbox__WritePartitionInfo__p_ffff0000907f5dec;
  }
  else {
    lVar1 = FUN_ffff00009077c144(lVar1,param_3,param_1,param_2);
    if (0 < lVar1) {
      return 0;
    }
    pcVar2 = s__blackbox__WritePartitionInfo__p_ffff0000907fcdcd;
  }
  FUN_ffff00009077e11c(pcVar2);
  return 1;
}


