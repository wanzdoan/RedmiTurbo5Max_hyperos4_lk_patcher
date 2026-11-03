// ffff0000907bcde8  size=136  FUN_ffff0000907bcde8

bool FUN_ffff0000907bcde8(undefined8 *param_1,undefined8 *param_2)

{
  undefined8 uVar1;
  ulong uVar2;
  undefined8 uVar3;
  
  uVar1 = FUN_ffff0000907c1060(*param_1);
  uVar3 = param_1[1];
  *param_2 = uVar1;
  uVar2 = FUN_ffff0000907c1060(uVar3);
  param_2[1] = uVar2;
  if ((uVar2 & 7) != 0) {
    uVar1 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_descript_ffff0000907ea432);
    FUN_ffff0000907c0ee8
              (uVar1,&DAT_ffff0000907ebfa6,&DAT_ffff0000907df116,s___ERROR__ffff0000907e07dc,
               s_Descriptor_size_is_not_divisible_ffff0000907d70bc,0);
  }
  return (uVar2 & 7) == 0;
}


