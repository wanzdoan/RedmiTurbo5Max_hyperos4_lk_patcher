// ffff00009071d2ac  size=124  FUN_ffff00009071d2ac

undefined8 FUN_ffff00009071d2ac(undefined8 param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009076d75c(param_1,s__mvpu_ffff0000907f7c58);
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__s__mvpu_not_found__so_searching_ffff0000907e0cd8,s_mvpu_prop_off_ffff0000907f5e1e)
    ;
    iVar1 = FUN_ffff00009076d75c(param_1,s_soc_mvpu_ffff0000907dc2ea);
    if (iVar1 < 0) {
      FUN_ffff00009077de54
                (s__s__mvpu_not_found_in_soc_ffff0000907d406e,s_mvpu_prop_off_ffff0000907f5e1e);
      return 2;
    }
  }
  return 0;
}


