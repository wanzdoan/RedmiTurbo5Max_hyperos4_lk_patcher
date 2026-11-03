// ffff00009070d56c  size=504  FUN_ffff00009070d56c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070d56c(char *param_1)

{
  char *pcVar1;
  bool bVar2;
  int iVar3;
  uint uVar4;
  ulong uVar5;
  undefined8 uVar6;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  ulong uStack_38;
  
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  if (DAT_ffff000090816d60 == '\0') {
    uVar6 = FUN_ffff000090709b2c(s_max_download_size_ffff0000907f79bc);
    uVar4 = FUN_ffff00009077f434(&local_70,0x40,s_Ultra_flash_Disabled____s__ffff0000907e241e,uVar6)
    ;
  }
  else {
    if (param_1 == (char *)0x0) {
      return;
    }
    for (; *param_1 == ' ' || *param_1 == '\t'; param_1 = param_1 + 1) {
    }
    uVar5 = FUN_ffff00009077f16c(param_1,0x23);
    if (0x1f < uVar5) {
      FUN_ffff000090709d10(s_name_too_long__ffff0000907d73ce);
      return;
    }
    FUN_ffff00009077eb88(0xffff00009083d39c,0,0x20);
    _DAT_ffff00009083df90 = FUN_ffff00009077f16c(param_1,0x1f);
    FUN_ffff00009077ef1c(0xffff00009083d39c,param_1,0x20);
    if (_DAT_ffff00009083df90 == 0) {
      bVar2 = false;
    }
    else {
      iVar3 = FUN_ffff00009077f0cc(s_system_ffff0000907ed94a,0xffff00009083d39c,0x20);
      if (iVar3 == 0) {
        bVar2 = true;
      }
      else {
        iVar3 = FUN_ffff00009077f0cc(s_super_ffff0000907e5a61,0xffff00009083d39c,0x20);
        bVar2 = iVar3 == 0;
      }
    }
    if (_DAT_ffff00009083df88 == (char *)0x0) {
      _DAT_ffff00009083df88 = (char *)FUN_ffff000090709b2c(s_max_download_size_ffff0000907f79bc);
    }
    pcVar1 = s_0xC0000000_ffff0000907fca02;
    if (!bVar2) {
      pcVar1 = _DAT_ffff00009083df88;
    }
    FUN_ffff000090709acc(s_max_download_size_ffff0000907f79bc,pcVar1);
    uVar6 = FUN_ffff000090709b2c(s_max_download_size_ffff0000907f79bc);
    uVar4 = FUN_ffff00009077f434
                      (&local_70,0x40,s__s____s__ffff0000907d5770,0xffff00009083d39c,uVar6);
  }
  if (0x3f < uVar4) {
    FUN_ffff00009077efe4(&local_70,s_Error__Response_buffer_overflow_ffff0000907dd84b,0x3f);
    uStack_38 = uStack_38 & 0xffffffffffffff;
  }
  FUN_ffff000090709c60(&local_70);
  FUN_ffff000090709d50(&DAT_ffff0000907df27e);
  return;
}


