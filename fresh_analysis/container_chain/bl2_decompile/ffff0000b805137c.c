
void FUN_ffff0000b805137c(void)

{
  int iVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  long lVar4;
  undefined8 uVar5;
  ulong uVar6;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  long local_80;
  long local_78;
  uint *local_70;
  undefined8 local_68;
  long local_60;
  undefined8 local_58;
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
  local_58 = 0;
  local_70 = (uint *)0x0;
  local_68 = 0;
  local_80 = 0;
  local_78 = 0;
  iVar1 = FUN_ffff0000b805159c(&local_80);
  if (iVar1 == 0) {
    FUN_ffff0000b8051824(&local_50);
    DAT_ffff0000b8133dac = 1;
    if (local_70 != (uint *)0x0) {
      DAT_ffff0000b8133dac = (uint)(5 < *local_70);
    }
    lVar4 = FUN_ffff0000b8027c48(s_logo_offset_ffff0000b8093d54);
    if (lVar4 == 0) {
      iVar1 = -1;
    }
    else {
      iVar1 = FUN_ffff0000b804bee4();
    }
    uVar5 = FUN_ffff0000b803e898();
    local_a0 = local_40;
    uStack_98 = uStack_38;
    local_90 = local_30;
    uStack_88 = uStack_28;
    local_b0 = local_50;
    uStack_a8 = uStack_48;
    uVar5 = FUN_ffff0000b8082d3c(0,uVar5,local_68,local_70,&local_b0,iVar1);
    if ((int)uVar5 != iVar1) {
      FUN_ffff0000b80518fc(uVar5,0);
    }
    uVar6 = FUN_ffff0000b8051340();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff0000b80519c0(&local_80), iVar1 != 0)) {
      uVar2 = thunk_FUN_ffff0000b803f20c();
      uVar3 = FUN_ffff0000b803e984();
      FUN_ffff0000b803e8ec(0,0,uVar2,uVar3);
    }
    else {
      FUN_ffff0000b8051a90(&DAT_ffff0000b8133db0);
      lVar4 = FUN_ffff0000b8027c48(s_logo_offset_ext_ffff0000b8085ab3);
      if (lVar4 == 0) {
        iVar1 = -1;
      }
      else {
        iVar1 = FUN_ffff0000b804bee4();
      }
      uVar5 = FUN_ffff0000b805135c();
      local_a0 = DAT_ffff0000b8133dc0;
      uStack_98 = DAT_ffff0000b8133dc8;
      local_90 = DAT_ffff0000b8133dd0;
      uStack_88 = DAT_ffff0000b8133dd8;
      local_b0 = DAT_ffff0000b8133db0;
      uStack_a8 = DAT_ffff0000b8133db8;
      uVar5 = FUN_ffff0000b8082d3c(0,uVar5,local_58,local_70,&local_b0,iVar1);
      if ((int)uVar5 != iVar1) {
        FUN_ffff0000b80518fc(uVar5,1);
      }
      uVar2 = thunk_FUN_ffff0000b803f20c();
      uVar3 = FUN_ffff0000b803e984();
      FUN_ffff0000b8051350(0,0,uVar2,uVar3,0,0,0,0,1);
    }
    if (local_80 != 0) {
      FUN_ffff0000b800d264();
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_70);
    }
    if (local_78 != 0) {
      FUN_ffff0000b800d264();
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_68);
    }
    if (local_60 != 0) {
      FUN_ffff0000b800d264();
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_58);
    }
  }
  return;
}


