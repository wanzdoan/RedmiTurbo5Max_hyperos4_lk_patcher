
/* WARNING: Type propagation algorithm not settling */

void FUN_ffff00009075c830(undefined8 param_1)

{
  uint uVar1;
  undefined4 uVar2;
  uint local_18 [2];
  
  uVar1 = (DAT_ffff00009087bf18 & 0xff00ff00) >> 8 | (DAT_ffff00009087bf18 & 0xff00ff) << 8;
  local_18[0] = uVar1 >> 0x10 | uVar1 << 0x10;
  uVar1 = (DAT_ffff00009087bf14 & 0xff00ff00) >> 8 | (DAT_ffff00009087bf14 & 0xff00ff) << 8;
  DAT_ffff00009087bf14 = uVar1 >> 0x10 | uVar1 << 0x10;
  local_18[1] = 0;
  uVar2 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  FUN_ffff00009076e6d4(param_1,uVar2,s_atag_videolfb_fb_base_h_ffff0000907f0289,local_18 + 1,4);
  FUN_ffff00009076e6d4(param_1,uVar2,s_atag_videolfb_fb_base_l_ffff0000907f6e18,local_18,4);
  FUN_ffff00009076e6d4
            (param_1,uVar2,s_atag_videolfb_vramSize_ffff0000907de81a,&DAT_ffff00009087bf14,4);
  return;
}


