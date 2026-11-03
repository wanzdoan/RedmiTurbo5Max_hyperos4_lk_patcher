// ffff0000907684c0  size=144  FUN_ffff0000907684c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907684c0(void)

{
  int iVar1;
  char *pcVar2;
  
  FUN_ffff00009077de54(s__FASTBOOT__Start_reset_dm_verity_ffff0000907e37b3);
  FUN_ffff0000907669ac(1);
  if ((_DAT_ffff00009090d910 == 0) && (iVar1 = FUN_ffff0000907c5644(), iVar1 != 0)) {
    FUN_ffff00009077de54(s__AVB__Failed_to_alloc_memory_for_ffff000090802c10,iVar1);
    pcVar2 = s__AVB__Failed_to_alloc_memory_for_ffff0000907d83d3;
  }
  else {
    iVar1 = FUN_ffff000090768368();
    if (iVar1 == 0) {
      FUN_ffff000090709d50(&DAT_ffff0000907df295);
      return;
    }
    FUN_ffff00009077de54(s__AVB__Failed_to_clear_dm_verity_s_ffff0000907e1d6e,iVar1);
    pcVar2 = s__AVB__Failed_to_clear_dm_verity_s_ffff0000907f6fe1;
  }
  FUN_ffff000090709d10(pcVar2);
  return;
}


