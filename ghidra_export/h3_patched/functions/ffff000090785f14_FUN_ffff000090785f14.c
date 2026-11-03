// ffff000090785f14  size=544  FUN_ffff000090785f14

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090785f14(void)

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
  iVar1 = FUN_ffff000090786134(&local_80);
  if (iVar1 == 0) {
    FUN_ffff0000907863bc(&local_50);
    _DAT_ffff0000908871a8 = 1;
    if (local_70 != (uint *)0x0) {
      _DAT_ffff0000908871a8 = (uint)(5 < *local_70);
    }
    lVar4 = FUN_ffff000090741944(s_logo_offset_ffff0000907e9f75);
    if (lVar4 == 0) {
      iVar1 = -1;
    }
    else {
      iVar1 = FUN_ffff00009077d464();
    }
    uVar5 = FUN_ffff00009075c370();
    local_a0 = local_40;
    uStack_98 = uStack_38;
    local_90 = local_30;
    uStack_88 = uStack_28;
    local_b0 = local_50;
    uStack_a8 = uStack_48;
    uVar5 = FUN_ffff0000907c32a0(0,uVar5,local_68,local_70,&local_b0,iVar1);
    if ((int)uVar5 != iVar1) {
      FUN_ffff000090786494(uVar5,0);
    }
    uVar6 = FUN_ffff000090785ed8();
    if (((uVar6 & 1) == 0) || (iVar1 = FUN_ffff000090786558(&local_80), iVar1 != 0)) {
      uVar2 = thunk_FUN_ffff00009075d4d0();
      uVar3 = FUN_ffff00009075c664();
      FUN_ffff00009075c518(0,0,uVar2,uVar3);
    }
    else {
      FUN_ffff000090786628(&DAT_ffff0000908871ac);
      lVar4 = FUN_ffff000090741944(s_logo_offset_ext_ffff0000907d02ac);
      if (lVar4 == 0) {
        iVar1 = -1;
      }
      else {
        iVar1 = FUN_ffff00009077d464();
      }
      uVar5 = FUN_ffff000090785ef4();
      local_a0 = _DAT_ffff0000908871bc;
      uStack_98 = _DAT_ffff0000908871c4;
      local_90 = _DAT_ffff0000908871cc;
      uStack_88 = _DAT_ffff0000908871d4;
      local_b0 = _DAT_ffff0000908871ac;
      uStack_a8 = _DAT_ffff0000908871b4;
      uVar5 = FUN_ffff0000907c32a0(0,uVar5,local_58,local_70,&local_b0,iVar1);
      if ((int)uVar5 != iVar1) {
        FUN_ffff000090786494(uVar5,1);
      }
      uVar2 = thunk_FUN_ffff00009075d4d0();
      uVar3 = FUN_ffff00009075c664();
      FUN_ffff000090785ee8(0,0,uVar2,uVar3,0,0,0,0,1);
    }
    if (local_80 != 0) {
      FUN_ffff00009071a6ac();
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_70);
    }
    if (local_78 != 0) {
      FUN_ffff00009071a6ac();
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_68);
    }
    if (local_60 != 0) {
      FUN_ffff00009071a6ac();
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_58);
    }
  }
  return;
}


