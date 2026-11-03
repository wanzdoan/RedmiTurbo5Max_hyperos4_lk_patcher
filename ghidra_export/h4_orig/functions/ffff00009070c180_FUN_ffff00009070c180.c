// ffff00009070c180  size=556  FUN_ffff00009070c180

undefined8 FUN_ffff00009070c180(void)

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
  FUN_ffff00009077ee50(0xffff00009083d310,0,200);
  uVar1 = thunk_FUN_ffff00009074b22c(0xc);
  uVar2 = thunk_FUN_ffff00009074b22c(0xd);
  uVar3 = thunk_FUN_ffff00009074b22c(0xe);
  uVar4 = thunk_FUN_ffff00009074b22c(0xf);
  iVar5 = FUN_ffff00009079482c(&local_e0);
  if (iVar5 == 0) {
    local_f0 = 0;
    uStack_e8 = 0;
    local_100 = 0;
    local_f8 = 0;
    lVar7 = FUN_ffff00009076fe0c((long)&uStack_d0 + 3,0x81,&local_100);
    if (lVar7 == 0) {
      pcVar6 = s_Failed_to_hash_ufsid__ffff0000907fb0c3;
    }
    else {
      FUN_ffff00009077f6fc
                (0xffff00009083d310,200,s__s_08x_08x_08x_08x_ffff0000907dbf7f,&DAT_ffff0000907df381,
                 uVar1,uVar2,uVar3,uVar4);
      FUN_ffff00009077f6fc(0xffff00009083d332,0xa6,&DAT_ffff0000907f5cad,local_100 & 0xff);
      FUN_ffff00009077f6fc(0xffff00009083d334,0xa4,&DAT_ffff0000907f5cad,local_100._1_1_);
      FUN_ffff00009077f6fc(0xffff00009083d336,0xa2,&DAT_ffff0000907f5cad,local_100._2_1_);
      FUN_ffff00009077f6fc(0xffff00009083d338,0xa0,&DAT_ffff0000907f5cad,local_100._3_1_);
      FUN_ffff00009077f6fc(0xffff00009083d33a,0x9e,&DAT_ffff0000907f5cad,local_100._4_1_);
      FUN_ffff00009077f6fc(0xffff00009083d33c,0x9c,&DAT_ffff0000907f5cad,local_100._5_1_);
      FUN_ffff00009077f6fc(0xffff00009083d33e,0x9a,&DAT_ffff0000907f5cad,local_100._6_1_);
      FUN_ffff00009077f6fc(0xffff00009083d340,0x98,&DAT_ffff0000907f5cad,local_100._7_1_);
      FUN_ffff00009077f6fc(0xffff00009083d342,0x96,&DAT_ffff0000907f5cad,local_f8 & 0xff);
      FUN_ffff00009077f6fc(0xffff00009083d344,0x94,&DAT_ffff0000907f5cad,local_f8._1_1_);
      FUN_ffff00009077f6fc(0xffff00009083d346,0x92,&DAT_ffff0000907f5cad,local_f8._2_1_);
      iVar5 = FUN_ffff00009077f6fc(0xffff00009083d348,0x90,&DAT_ffff0000907f5cad,local_f8._3_1_);
      if (-1 < iVar5) {
        return 0xffff00009083d310;
      }
      pcVar6 = s_Failed_to_format_cpuid__ffff0000907d73bc;
    }
  }
  else {
    pcVar6 = s_Failed_to_get_ufsid__ffff0000908003d6;
  }
  FUN_ffff00009077e11c(pcVar6);
  return 0xffff00009083d310;
}


