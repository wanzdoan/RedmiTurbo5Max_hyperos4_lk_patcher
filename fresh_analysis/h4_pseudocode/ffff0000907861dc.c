
void FUN_ffff0000907861dc(void)

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
  iVar1 = FUN_ffff0000907863fc(&local_80);
  if (iVar1 == 0) {
    FUN_ffff000090786684(&local_50);
    DAT_ffff000090887240 = 1;
    if (local_70 != (uint *)0x0) {
      DAT_ffff000090887240 = (uint)(5 < *local_70);
    }
    lVar4 = FUN_ffff000090741bdc(s_logo_offset_ffff0000907ea0d0);
    if (lVar4 == 0) {
      iVar1 = -1;
    }
    else {
      iVar1 = FUN_ffff00009077d72c();
    }
    uVar5 = FUN_ffff00009075c608();
    local_a0 = local_40;
    uStack_98 = uStack_38;
    local_90 = local_30;
    uStack_88 = uStack_28;
    local_b0 = local_50;
    uStack_a8 = uStack_48;
    uVar5 = FUN_ffff0000907c3914(0,uVar5,local_68,local_70,&local_b0,iVar1);
    if ((int)uVar5 != iVar1) {
      FUN_ffff00009078675c(uVar5,0);
    }
    uVar6 = FUN_ffff0000907861a0();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff000090786820(&local_80), iVar1 != 0)) {
      uVar2 = thunk_FUN_ffff00009075d768();
      uVar3 = FUN_ffff00009075c8fc();
      FUN_ffff00009075c7b0(0,0,uVar2,uVar3);
    }
    else {
      FUN_ffff0000907868f0(&DAT_ffff000090887244);
      lVar4 = FUN_ffff000090741bdc(s_logo_offset_ext_ffff0000907d02cb);
      if (lVar4 == 0) {
        iVar1 = -1;
      }
      else {
        iVar1 = FUN_ffff00009077d72c();
      }
      uVar5 = FUN_ffff0000907861bc();
      local_a0 = DAT_ffff000090887254;
      uStack_98 = DAT_ffff00009088725c;
      local_90 = DAT_ffff000090887264;
      uStack_88 = DAT_ffff00009088726c;
      local_b0 = DAT_ffff000090887244;
      uStack_a8 = DAT_ffff00009088724c;
      uVar5 = FUN_ffff0000907c3914(0,uVar5,local_58,local_70,&local_b0,iVar1);
      if ((int)uVar5 != iVar1) {
        FUN_ffff00009078675c(uVar5,1);
      }
      uVar2 = thunk_FUN_ffff00009075d768();
      uVar3 = FUN_ffff00009075c8fc();
      FUN_ffff0000907861b0(0,0,uVar2,uVar3,0,0,0,0,1);
    }
    if (local_80 != 0) {
      FUN_ffff00009071a7f0();
      FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_70);
    }
    if (local_78 != 0) {
      FUN_ffff00009071a7f0();
      FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_68);
    }
    if (local_60 != 0) {
      FUN_ffff00009071a7f0();
      FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_58);
    }
  }
  return;
}


