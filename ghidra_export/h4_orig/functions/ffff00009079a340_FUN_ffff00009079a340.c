// ffff00009079a340  size=408  FUN_ffff00009079a340

undefined4 FUN_ffff00009079a340(void)

{
  int iVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 local_a0;
  undefined8 local_98;
  undefined8 local_90;
  ulong local_88;
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
  local_b0 = 0;
  local_a8 = 0xbb67ae856a09e667;
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
  local_a0 = 0xa54ff53a3c6ef372;
  local_98 = 0x9b05688c510e527f;
  local_90 = 0x5be0cd191f83d9ab;
  local_88 = 0;
  uVar2 = FUN_ffff00009077f250(&DAT_ffff0000907f228e);
  FUN_ffff000090799f14(&local_b0,&DAT_ffff0000907f228e,uVar2);
  FUN_ffff00009079a03c(&local_b0,&local_40);
  iVar1 = FUN_ffff0000907991c0
                    (&local_40,0x20,&DAT_ffff000090837c30,0x20,s_SHA256_ffff0000907d21da,0);
  uVar3 = 5;
  if (iVar1 == 0) {
    local_a8 = 0xbb67ae856a09e667;
    local_a0 = 0xa54ff53a3c6ef372;
    local_88 = local_88 & 0xffffffff00000000;
    local_b0 = 0;
    local_98 = 0x9b05688c510e527f;
    local_90 = 0x5be0cd191f83d9ab;
    uVar2 = FUN_ffff00009077f250(s_abcdbcdecdefdefgefghfghighijhijk_ffff0000907dbbdc);
    FUN_ffff000090799f14(&local_b0,s_abcdbcdecdefdefgefghfghighijhijk_ffff0000907dbbdc,uVar2);
    FUN_ffff00009079a03c(&local_b0,&local_40);
    iVar1 = FUN_ffff0000907991c0
                      (&local_40,0x20,&DAT_ffff000090837c58,0x20,s_SHA256_ffff0000907d21da,1);
    uVar3 = 0;
    if (iVar1 != 0) {
      uVar3 = 5;
    }
  }
  return uVar3;
}


