
undefined8 FUN_ffff0000907b5e84(undefined8 param_1,undefined4 param_2,long param_3)

{
  byte bVar1;
  bool bVar2;
  undefined4 uVar3;
  undefined8 uVar4;
  ulong uVar5;
  ulong uVar6;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  ulong uStack_58;
  byte local_50 [32];
  
  local_50[0x10] = 0;
  local_50[0x11] = 0;
  local_50[0x12] = 0;
  local_50[0x13] = 0;
  local_50[0x14] = 0;
  local_50[0x15] = 0;
  local_50[0x16] = 0;
  local_50[0x17] = 0;
  local_50[0x18] = 0;
  local_50[0x19] = 0;
  local_50[0x1a] = 0;
  local_50[0x1b] = 0;
  local_50[0x1c] = 0;
  local_50[0x1d] = 0;
  local_50[0x1e] = 0;
  local_50[0x1f] = 0;
  local_50[0] = 0;
  local_50[1] = 0;
  local_50[2] = 0;
  local_50[3] = 0;
  local_50[4] = 0;
  local_50[5] = 0;
  local_50[6] = 0;
  local_50[7] = 0;
  local_50[8] = 0;
  local_50[9] = 0;
  local_50[10] = 0;
  local_50[0xb] = 0;
  local_50[0xc] = 0;
  local_50[0xd] = 0;
  local_50[0xe] = 0;
  local_50[0xf] = 0;
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
  FUN_ffff0000907ad2dc(&local_60,&DAT_ffff0000907d542d,0x10);
  uStack_58 = uStack_58 & 0xffffffffffffff;
  uVar3 = FUN_ffff0000907ad25c(&local_60);
  uVar4 = FUN_ffff0000907a28ec(param_1,param_2,&local_a0,DAT_ffff00009090d7fc);
  if ((int)uVar4 == 0) {
    uVar4 = FUN_ffff000090799010
                      (&DAT_ffff000090813430,0x40,&local_60,uVar3,&local_a0,DAT_ffff00009090d800,
                       local_50,0x20);
    if ((int)uVar4 == 0) {
      uVar5 = 0;
      uVar6 = 0;
      do {
        bVar1 = local_50[uVar6];
        *(char *)(param_3 + uVar5) = s_0123456789abcdef_ffff000090804c70[bVar1 >> 4];
        if (0x27 < uVar5) break;
        bVar2 = uVar6 < 0x13;
        *(char *)(param_3 + uVar5 + 1) = s_0123456789abcdef_ffff000090804c70[(ulong)bVar1 & 0xf];
        uVar5 = uVar5 + 2;
        uVar6 = uVar6 + 1;
      } while (bVar2);
      uVar4 = 0;
      *(undefined1 *)(param_3 + 0x28) = 0;
    }
  }
  return uVar4;
}


