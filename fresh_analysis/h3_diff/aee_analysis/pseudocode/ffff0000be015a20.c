
undefined * FUN_ffff0000be015a20(void)

{
  undefined4 uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  int iVar5;
  char *pcVar6;
  long lVar7;
  undefined8 local_100;
  undefined8 local_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined2 local_48;
  
  local_48 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e0 = 0;
  FUN_ffff0000be060ae0(&DAT_ffff0000be107a7c,0,200);
  uVar1 = thunk_FUN_ffff0000be0364a4(0xc);
  uVar2 = thunk_FUN_ffff0000be0364a4(0xd);
  uVar3 = thunk_FUN_ffff0000be0364a4(0xe);
  uVar4 = thunk_FUN_ffff0000be0364a4(0xf);
  iVar5 = FUN_ffff0000be0721b0(&local_e0);
  if (iVar5 == 0) {
    local_f0 = 0;
    uStack_e8 = 0;
    local_100 = 0;
    local_f8 = 0;
    lVar7 = FUN_ffff0000be057568((long)&uStack_d0 + 3,0x81,&local_100);
    if (lVar7 == 0) {
      pcVar6 = s_Failed_to_hash_ufsid__ffff0000be0c18fb;
    }
    else {
      FUN_ffff0000be0611dc
                (&DAT_ffff0000be107a7c,200,s__s_08x_08x_08x_08x_ffff0000be0ac602,
                 &DAT_ffff0000be0ae566,uVar1,uVar2,uVar3,uVar4);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107a9e,0xa6,&DAT_ffff0000be0be09d,local_100 & 0xff);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107aa0,0xa4,&DAT_ffff0000be0be09d,local_100._1_1_);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107aa2,0xa2,&DAT_ffff0000be0be09d,local_100._2_1_);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107aa4,0xa0,&DAT_ffff0000be0be09d,local_100._3_1_);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107aa6,0x9e,&DAT_ffff0000be0be09d,local_100._4_1_);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107aa8,0x9c,&DAT_ffff0000be0be09d,local_100._5_1_);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107aaa,0x9a,&DAT_ffff0000be0be09d,local_100._6_1_);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107aac,0x98,&DAT_ffff0000be0be09d,local_100._7_1_);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107aae,0x96,&DAT_ffff0000be0be09d,local_f8 & 0xff);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107ab0,0x94,&DAT_ffff0000be0be09d,local_f8._1_1_);
      FUN_ffff0000be0611dc(&DAT_ffff0000be107ab2,0x92,&DAT_ffff0000be0be09d,local_f8._2_1_);
      iVar5 = FUN_ffff0000be0611dc(&DAT_ffff0000be107ab4,0x90,&DAT_ffff0000be0be09d,local_f8._3_1_);
      if (-1 < iVar5) {
        return &DAT_ffff0000be107a7c;
      }
      pcVar6 = s_Failed_to_format_cpuid__ffff0000be0a8e49;
    }
  }
  else {
    pcVar6 = s_Failed_to_get_ufsid__ffff0000be0c4f29;
  }
  FUN_ffff0000be05fd98(pcVar6);
  return &DAT_ffff0000be107a7c;
}


