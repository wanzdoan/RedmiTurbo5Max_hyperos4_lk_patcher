// ffff000090771d14  size=292  FUN_ffff000090771d14

undefined8 FUN_ffff000090771d14(undefined8 param_1,undefined8 param_2,long *param_3)

{
  uint uVar1;
  long lVar2;
  long lVar3;
  uint local_14;
  
  *param_3 = 0;
  lVar2 = FUN_ffff000090770f60(param_2,s_target_ffff0000907f7235,0);
  if (lVar2 != 0) {
    local_14 = 0;
    thunk_FUN_ffff00009077eb20(&local_14,lVar2,4);
    uVar1 = (local_14 & 0xff00ff00) >> 8 | (local_14 & 0xff00ff) << 8;
    local_14 = uVar1 >> 0x10 | uVar1 << 0x10;
    lVar2 = FUN_ffff000090770214(param_1);
    *param_3 = lVar2;
    if (lVar2 != 0) {
      return 0;
    }
    FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_overlay_get_target_ffff0000907deb91)
    ;
    FUN_ffff00009076fec8(s_failed_to_find_target__04x_ffff0000907deba9,local_14);
    return 4;
  }
  if (*param_3 == 0) {
    lVar2 = FUN_ffff000090770f60(param_2,s_target_path_ffff0000907f723c,0);
    if (lVar2 == 0) {
      return 1;
    }
    lVar3 = FUN_ffff0000907701d8(param_1,lVar2);
    *param_3 = lVar3;
    if (lVar3 == 0) {
      FUN_ffff00009076fec8
                (s_ERROR___s____ffff0000907f36ba,s_ufdt_overlay_get_target_ffff0000907deb91);
      FUN_ffff00009076fec8(s_failed_to_find_target_path__s_ffff0000907f1e09,lVar2);
      return 3;
    }
  }
  return 0;
}


