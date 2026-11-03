
void FUN_ffff0000907869a0(undefined4 param_1)

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
  FUN_ffff00009077e11c
            (s__lk_logo___s__d_logo_index____d_ffff000090802fd6,
             s_mt_disp_show_logo_by_index_ffff0000907eee33,0x28c,param_1);
  if (DAT_ffff000090887278 == (undefined *)0x0) {
    FUN_ffff00009077ee50(&DAT_ffff000090887280,0,0x28);
    DAT_ffff000090887278 = &DAT_ffff000090887280;
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
    goto code_r0xffff000090786b74;
  default:
    uVar6 = 5;
  }
  local_70 = 0;
  local_68 = 0;
  local_80 = (uint *)0x0;
  local_78 = 0;
  local_90 = 0;
  local_88 = 0;
  iVar1 = FUN_ffff0000907863fc(&local_90);
  if (iVar1 == 0) {
    FUN_ffff000090786684(&local_60);
    DAT_ffff000090887240 = 1;
    if (local_80 != (uint *)0x0) {
      DAT_ffff000090887240 = (uint)(5 < *local_80);
    }
    lVar4 = FUN_ffff000090741bdc(s_logo_offset_ffff0000907ea0d0);
    if (lVar4 == 0) {
      iVar1 = -1;
    }
    else {
      iVar1 = FUN_ffff00009077d72c();
    }
    uVar5 = FUN_ffff00009075c3e4();
    local_b0 = local_50;
    uStack_a8 = uStack_48;
    local_a0 = local_40;
    uStack_98 = uStack_38;
    local_c0 = local_60;
    uStack_b8 = uStack_58;
    uVar6 = FUN_ffff0000907c3914(uVar6,uVar5,local_78,local_80,&local_c0,iVar1);
    if ((int)uVar6 != iVar1) {
      FUN_ffff00009078675c(uVar6,0);
    }
    uVar2 = thunk_FUN_ffff00009075d768();
    uVar3 = FUN_ffff00009075c8fc();
    FUN_ffff00009075c65c(0,0,uVar2,uVar3);
    if (local_90 != 0) {
      FUN_ffff00009071a7f0();
      FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_80);
    }
    if (local_88 != 0) {
      FUN_ffff00009071a7f0();
      FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_78);
    }
    if (local_70 != 0) {
      FUN_ffff00009071a7f0();
      FUN_ffff00009077be30(PTR_DAT_ffff000090839928,local_68);
    }
  }
code_r0xffff000090786b74:
  return;
}


