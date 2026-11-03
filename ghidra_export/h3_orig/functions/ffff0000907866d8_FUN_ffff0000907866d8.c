// ffff0000907866d8  size=492  FUN_ffff0000907866d8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907866d8(undefined4 param_1)

{
  int iVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  long lVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  long local_90;
  long local_88;
  uint *local_80;
  undefined8 local_78;
  long local_70;
  undefined8 local_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  FUN_ffff00009077de54
            (s__lk_logo___s__d_logo_index____d_ffff000090802dd2,
             s_mt_disp_show_logo_by_index_ffff0000907eecc2,0x262,param_1);
  if (_DAT_ffff0000908871e0 == 0) {
    FUN_ffff00009077eb88(0xffff0000908871e8,0,0x28);
    _DAT_ffff0000908871e0 = -0xffff6f778e18;
  }
  uVar6 = 2;
  switch(param_1) {
  case 0:
  case 3:
    break;
  case 1:
    uVar6 = 1;
    break;
  case 2:
  case 4:
    uVar6 = 3;
    break;
  case 5:
    goto code_r0xffff0000907868ac;
  default:
    uVar6 = 5;
  }
  local_70 = 0;
  local_68 = 0;
  local_80 = (uint *)0x0;
  local_78 = 0;
  local_90 = 0;
  local_88 = 0;
  iVar1 = FUN_ffff000090786134(&local_90);
  if (iVar1 == 0) {
    FUN_ffff0000907863bc(&local_60);
    _DAT_ffff0000908871a8 = 1;
    if (local_80 != (uint *)0x0) {
      _DAT_ffff0000908871a8 = (uint)(5 < *local_80);
    }
    lVar4 = FUN_ffff000090741944(s_logo_offset_ffff0000907e9f8c);
    if (lVar4 == 0) {
      iVar1 = -1;
    }
    else {
      iVar1 = FUN_ffff00009077d464();
    }
    uVar5 = FUN_ffff00009075c14c();
    local_b0 = local_50;
    uStack_a8 = uStack_48;
    local_a0 = local_40;
    uStack_98 = uStack_38;
    local_c0 = local_60;
    uStack_b8 = uStack_58;
    uVar6 = FUN_ffff0000907c32a0(uVar6,uVar5,local_78,local_80,&local_c0,iVar1);
    if ((int)uVar6 != iVar1) {
      FUN_ffff000090786494(uVar6,0);
    }
    uVar2 = thunk_FUN_ffff00009075d4d0();
    uVar3 = FUN_ffff00009075c664();
    FUN_ffff00009075c3c4(0,0,uVar2,uVar3);
    if (local_90 != 0) {
      FUN_ffff00009071a6ac();
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_80);
    }
    if (local_88 != 0) {
      FUN_ffff00009071a6ac();
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_78);
    }
    if (local_70 != 0) {
      FUN_ffff00009071a6ac();
      FUN_ffff00009077bb68(DAT_ffff000090839908,local_68);
    }
  }
code_r0xffff0000907868ac:
  return;
}


