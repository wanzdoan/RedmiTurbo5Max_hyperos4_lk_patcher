
undefined8 FUN_ffff0000b807961c(undefined8 param_1,undefined8 param_2,long *param_3)

{
  uint uVar1;
  long lVar2;
  long lVar3;
  uint local_14;
  
  *param_3 = 0;
  lVar2 = FUN_ffff0000b8078868(param_2,s_target_ffff0000b809b2c7,0);
  if (lVar2 != 0) {
    local_14 = 0;
    thunk_FUN_ffff0000b804cd40(&local_14,lVar2,4);
    uVar1 = (local_14 & 0xff00ff00) >> 8 | (local_14 & 0xff00ff) << 8;
    local_14 = uVar1 >> 0x10 | uVar1 << 0x10;
    lVar2 = FUN_ffff0000b8077b1c(param_1);
    *param_3 = lVar2;
    if (lVar2 != 0) {
      return 0;
    }
    FUN_ffff0000b80777d0(s_ERROR___s____ffff0000b809976e,s_ufdt_overlay_get_target_ffff0000b808da1b)
    ;
    FUN_ffff0000b80777d0(s_failed_to_find_target__04x_ffff0000b808da33,local_14);
    return 4;
  }
  if (*param_3 == 0) {
    lVar2 = FUN_ffff0000b8078868(param_2,s_target_path_ffff0000b809b2ce,0);
    if (lVar2 == 0) {
      return 1;
    }
    lVar3 = FUN_ffff0000b8077ae0(param_1,lVar2);
    *param_3 = lVar3;
    if (lVar3 == 0) {
      FUN_ffff0000b80777d0
                (s_ERROR___s____ffff0000b809976e,s_ufdt_overlay_get_target_ffff0000b808da1b);
      FUN_ffff0000b80777d0(s_failed_to_find_target_path__s_ffff0000b8098917,lVar2);
      return 3;
    }
  }
  return 0;
}


