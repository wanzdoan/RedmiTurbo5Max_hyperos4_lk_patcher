// ffff00009075c598  size=180  FUN_ffff00009075c598

/* WARNING: Type propagation algorithm not settling */
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c598(undefined8 param_1)

{
  uint uVar1;
  undefined4 uVar2;
  uint local_18 [2];
  
  uVar1 = (_DAT_ffff00009087be80 & 0xff00ff00) >> 8 | (_DAT_ffff00009087be80 & 0xff00ff) << 8;
  local_18[0] = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (_DAT_ffff00009087be7c & 0xff00ff00) >> 8 | (_DAT_ffff00009087be7c & 0xff00ff) << 8;
  _DAT_ffff00009087be7c = uVar1 >> 0x10 | uVar1 << 0x10;
  local_18[1] = 0;
  uVar2 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1);
  FUN_ffff00009076e40c(param_1,uVar2,s_atag_videolfb_fb_base_h_ffff0000907f0118,local_18 + 1,4);
  FUN_ffff00009076e40c(param_1,uVar2,s_atag_videolfb_fb_base_l_ffff0000907f6c8e,local_18,4);
  FUN_ffff00009076e40c
            (param_1,uVar2,s_atag_videolfb_vramSize_ffff0000907de777,&DAT_ffff00009087be7c,4);
  return;
}


