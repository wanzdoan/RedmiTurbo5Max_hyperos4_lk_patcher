// ffff00009071d438  size=124  FUN_ffff00009071d438

undefined8 FUN_ffff00009071d438(undefined8 param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009076da24(param_1,s__mvpu_ffff0000907f7df4);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c
              (s__s__mvpu_not_found__so_searching_ffff0000907e0d92,s_mvpu_prop_off_ffff0000907f5fbf)
    ;
    iVar1 = FUN_ffff00009076da24(param_1,s_soc_mvpu_ffff0000907dc3d7);
    if (iVar1 < 0) {
      FUN_ffff00009077e11c
                (s__s__mvpu_not_found_in_soc_ffff0000907d40ab,s_mvpu_prop_off_ffff0000907f5fbf);
      return 2;
    }
  }
  return 0;
}


