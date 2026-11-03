
void FUN_ffff00009075dd10(undefined4 param_1)

{
  ulong uVar1;
  undefined4 uVar2;
  int iVar3;
  undefined8 local_a40;
  undefined8 local_a38;
  undefined8 uStack_a30;
  undefined4 local_a28;
  undefined4 uStack_a24;
  undefined4 local_a20;
  undefined4 local_a1c;
  undefined8 local_a18;
  undefined8 local_a10;
  undefined8 local_a08;
  undefined8 local_a00;
  undefined8 local_9f8;
  undefined8 uStack_9f0;
  undefined8 local_9e8;
  undefined8 uStack_9e0;
  undefined4 local_9d8 [6];
  undefined4 auStack_9c0 [5];
  undefined8 uStack_9ac;
  undefined4 auStack_9a4 [4];
  undefined8 uStack_994;
  undefined4 auStack_98c [5];
  undefined1 auStack_978 [8];
  undefined4 auStack_970 [560];
  undefined4 local_b0;
  undefined4 local_ac;
  
  FUN_ffff00009077ee50(local_9d8,0,0x998);
  local_9e8 = 0;
  uStack_9e0 = 0;
  local_9f8 = 0;
  uStack_9f0 = 0;
  local_a08 = 0;
  local_a00 = 0;
  local_a18 = 0;
  local_a10 = 0;
  local_a28 = 0;
  uStack_a24 = 0;
  local_a20 = 0;
  local_a1c = 0;
  local_a38 = 0;
  uStack_a30 = 0;
  local_a40 = 0;
  FUN_ffff00009077ee50(local_9d8,0,0x998);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  local_b0 = FUN_ffff00009075c0e8(DAT_ffff00009087bf70);
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  local_ac = FUN_ffff00009075c128(DAT_ffff00009087bf70);
  local_9d8[0] = 1;
  FUN_ffff00009077ee50(&local_a40,0,0x68);
  uStack_a24 = 0;
  local_a20 = 0;
  local_a40 = 0x100000000;
  local_a38._4_4_ = param_1;
  local_a1c = thunk_FUN_ffff00009075d768();
  uVar2 = FUN_ffff00009075c8fc();
  local_a18 = CONCAT44(local_a18._4_4_,uVar2);
  iVar3 = thunk_FUN_ffff00009075d768();
  local_a10 = 0;
  local_a18 = CONCAT44(iVar3 * 4 + 0x7c,(undefined4)local_a18) & 0xffffff80ffffffff;
  uVar2 = thunk_FUN_ffff00009075d768();
  local_a08 = CONCAT44(local_a08._4_4_,uVar2);
  uVar2 = FUN_ffff00009075c8fc();
  local_a08 = CONCAT44(uVar2,(undefined4)local_a08);
  uVar1 = local_a40 & 0xffffffff;
  local_9f8 = CONCAT35(local_9f8._5_3_,0xff00000000);
  local_9f8 = CONCAT44(local_9f8._4_4_,1);
  auStack_9c0[uVar1 * 0x1a] = (undefined4)local_a40;
  auStack_9c0[uVar1 * 0x1a + 1] = local_a40._4_4_;
  auStack_9c0[uVar1 * 0x1a + 3] = (undefined4)local_a38;
  auStack_9c0[uVar1 * 0x1a + 4] = local_a38._4_4_;
  (&uStack_9ac)[uVar1 * 0xd] = CONCAT44(uStack_a24,local_a28);
  auStack_978[uVar1 * 0x68] = 0xff;
  auStack_9a4[uVar1 * 0x1a] = local_a20;
  auStack_9a4[uVar1 * 0x1a + 1] = local_a1c;
  auStack_98c[uVar1 * 0x1a + 4] = 1;
  auStack_9a4[uVar1 * 0x1a + 2] = (undefined4)local_a18;
  auStack_9a4[uVar1 * 0x1a + 3] = local_a18._4_4_;
  (&uStack_994)[uVar1 * 0xd] = local_a10;
  auStack_98c[uVar1 * 0x1a] = (undefined4)local_a08;
  auStack_98c[uVar1 * 0x1a + 1] = uVar2;
  auStack_970[uVar1 * 0x1a + 4] = uStack_9e0._4_4_;
  auStack_98c[uVar1 * 0x1a + 2] = (undefined4)local_a00;
  auStack_98c[uVar1 * 0x1a + 3] = local_a00._4_4_;
  auStack_970[uVar1 * 0x1a] = uStack_9f0._4_4_;
  auStack_970[uVar1 * 0x1a + 1] = (undefined4)local_9e8;
  auStack_970[uVar1 * 0x1a + 2] = local_9e8._4_4_;
  auStack_970[uVar1 * 0x1a + 3] = (undefined4)uStack_9e0;
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  iVar3 = FUN_ffff00009075650c(DAT_ffff00009087bf88);
  if (iVar3 != 0) {
    if ((DAT_ffff00009087a99c & 1) == 0) {
      FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
      DAT_ffff00009087a99c = 1;
    }
    iVar3 = FUN_ffff00009075c250(DAT_ffff00009087bf70);
    if (iVar3 != 0) {
      if ((DAT_ffff00009087a99c & 1) == 0) {
        FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
        DAT_ffff00009087a99c = 1;
      }
      FUN_ffff000090756b18(DAT_ffff00009087bf88,1,100);
    }
  }
  if ((DAT_ffff00009087a99c & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087bf60,0,0x38);
    DAT_ffff00009087a99c = 1;
  }
  FUN_ffff0000907565bc(DAT_ffff00009087bf88,local_9d8,param_1);
  return;
}


