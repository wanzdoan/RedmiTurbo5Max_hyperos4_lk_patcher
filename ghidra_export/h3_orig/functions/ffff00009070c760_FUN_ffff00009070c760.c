// ffff00009070c760  size=264  FUN_ffff00009070c760

void FUN_ffff00009070c760(void)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  ulong uVar4;
  ulong uVar5;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  ulong uStack_98;
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
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  lVar3 = FUN_ffff000090788dc8();
  uVar2 = FUN_ffff00009077ef88();
  uVar4 = 0;
  uVar5 = (ulong)uVar2 / 0x28;
  uVar1 = uVar2 % 0x28;
  if (0x27 < uVar2) {
    uVar4 = 0;
    do {
      uVar2 = (int)uVar5 - 1;
      uVar5 = (ulong)uVar2;
      FUN_ffff00009077e858(&local_c0,s_token__ffff0000907e730d,7);
      FUN_ffff00009077e858((ulong)&local_c0 | 7,lVar3 + uVar4,0x28);
      uStack_98 = uStack_98 & 0xffffffffffffff;
      FUN_ffff000090709c60(&local_c0);
      uVar4 = (ulong)((int)uVar4 + 0x28);
    } while (uVar2 != 0);
  }
  if (uVar1 != 0) {
    FUN_ffff00009077e858((ulong)&local_c0 | 7,lVar3 + uVar4,(ulong)uVar1);
    *(undefined1 *)((long)&local_c0 + (ulong)uVar1 + 7) = 0;
    FUN_ffff000090709c60(&local_c0);
  }
  FUN_ffff000090709d50(&DAT_ffff0000907df295);
  return;
}


