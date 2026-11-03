// ffff0000907691ec  size=180  FUN_ffff0000907691ec

int FUN_ffff0000907691ec(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  char *pcVar3;
  
  uVar2 = FUN_ffff00009077ef88();
  pcVar3 = s_androidboot_vbmeta_hash_alg_ffff0000907e9d31;
  iVar1 = FUN_ffff0000907c5854(param_1,uVar2,s_androidboot_vbmeta_hash_alg_ffff0000907e9d31);
  if (iVar1 == 0) {
    uVar2 = FUN_ffff00009077ef88(param_1);
    pcVar3 = s_androidboot_vbmeta_size_ffff0000907fdfb4;
    iVar1 = FUN_ffff0000907c5854(param_1,uVar2,s_androidboot_vbmeta_size_ffff0000907fdfb4);
    if (iVar1 == 0) {
      uVar2 = FUN_ffff00009077ef88(param_1);
      pcVar3 = s_androidboot_vbmeta_digest_ffff0000907e9d4d;
      iVar1 = FUN_ffff0000907c5854(param_1,uVar2,s_androidboot_vbmeta_digest_ffff0000907e9d4d);
      if (iVar1 == 0) {
        return 0;
      }
    }
  }
  FUN_ffff00009077de54(s__AVB__Extract__s_fails_ffff0000907d83fd,pcVar3);
  return iVar1;
}


