// ffff0000907c2d68  size=268  FUN_ffff0000907c2d68

undefined8 FUN_ffff0000907c2d68(undefined8 param_1,undefined8 param_2,long param_3)

{
  int iVar1;
  ulong uVar2;
  undefined8 uVar3;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  ulong local_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  local_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  uStack_128 = 0;
  local_140 = 0;
  uStack_138 = 0;
  uVar2 = FUN_ffff0000907c2afc(param_1,param_2,&local_120,&local_140,0);
  if ((uVar2 & 1) != 0) {
    iVar1 = thunk_FUN_ffff00009077eae8(&local_120,&DAT_ffff0000907e3ea0,4);
    if (iVar1 == 0) {
      uVar2 = FUN_ffff0000907c1050(local_a8 & 0xffffffff);
      if (param_3 != 0) {
        *(bool *)param_3 = (uVar2 & 2) == 0;
        return 1;
      }
      return 1;
    }
    uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_user_avb_use_ffff0000907d07cc);
    FUN_ffff0000907c0ee8
              (uVar3,&DAT_ffff0000907ebfa6,&DAT_ffff0000907dd788,s___ERROR__ffff0000907e07dc,
               s_Data_from___ffff0000907e3f31,&local_140,
               s___does_not_look_like_a_vbmeta_he_ffff0000907d07a5,0);
  }
  return 0;
}


