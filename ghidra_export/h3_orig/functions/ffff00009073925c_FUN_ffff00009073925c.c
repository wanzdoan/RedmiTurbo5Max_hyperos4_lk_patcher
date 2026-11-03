// ffff00009073925c  size=588  FUN_ffff00009073925c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff00009073925c(undefined8 param_1,undefined4 param_2)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  undefined4 uVar6;
  uint uVar7;
  
  if (_DAT_ffff00001c800004 == 0) {
    FUN_ffff00009077de54(s__SCP__scp_region_info___0__skip_c_ffff0000907ee16a);
    uVar6 = 0;
  }
  else {
    uVar3 = _DAT_ffff00009086de24 - _DAT_ffff00009086de20;
    iVar5 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_SCP_reserved_ffff0000907f4747,uVar3 + 0x700000,
                       &DAT_ffff00009086de08,0xc,_DAT_ffff00001c800004,0,0);
    lVar4 = _DAT_ffff00009086de08;
    if (iVar5 == 0) {
      FUN_ffff00009077eb88(_DAT_ffff00009086de08,0,0x700000);
      FUN_ffff00009077e858
                (_DAT_ffff00009086de08 + (ulong)_DAT_ffff00009086de10,0xffff00001c800000,
                 _DAT_ffff00009086de14 - _DAT_ffff00009086de10);
      uVar7 = _DAT_ffff00001cf2a000;
      lVar1 = _DAT_ffff00009086de08 + (ulong)_DAT_ffff00009086de14;
      _DAT_ffff00001cf2a000 = _DAT_ffff00001cf2a000 & 0x7fffffff | 0x30000000;
      FUN_ffff00009077e858(lVar1,0xffff00001cf60000,0x1c800);
      _DAT_ffff00001cf2a000 = uVar7 | 0xb0000000;
      FUN_ffff00009077e858(lVar1 + 0x72000,0xffff00001cf60000,0x1f800);
      _DAT_ffff00001cf2a000 = uVar7;
      FUN_ffff000090738d20(lVar1 + 0x3c000,_DAT_ffff00009086de08 + (ulong)_DAT_ffff00009086de18);
      FUN_ffff000090738bd0
                (_DAT_ffff00009086de08 + (ulong)_DAT_ffff00009086de18,
                 _DAT_ffff00009086de08 + (ulong)_DAT_ffff00009086de1c);
      FUN_ffff000090738e44(_DAT_ffff00009086de08 + (ulong)_DAT_ffff00009086de1c);
      uVar7 = _DAT_ffff00009086de20;
      uVar2 = _DAT_ffff00001c800018;
      if ((int)_DAT_ffff00001c800018 < 1) {
        FUN_ffff00009077de54(s__SCP__ap_dram_size_<_0_ffff0000907d11ac);
      }
      else {
        FUN_ffff00009077de54(s__SCP__ap_dram_size__x_ffff0000907fefc2,_DAT_ffff00001c800018);
        if (uVar3 <= uVar2) {
          uVar2 = uVar3;
        }
        FUN_ffff00009077e858
                  (_DAT_ffff00009086de08 + (ulong)_DAT_ffff00009086de20,lVar4 + 0x700000,uVar2);
        uVar7 = (uVar2 + 3 & 0xfffffffc) + uVar7;
      }
      FUN_ffff00009077de54(s__SCP__scp_dump_size_total___0x_x_ffff0000907ee194,uVar7,param_2);
      uVar6 = FUN_ffff000090739040(lVar4,param_1,uVar7,param_2);
      FUN_ffff00009077bb68(DAT_ffff000090839908,_DAT_ffff00009086de08);
    }
    else {
      FUN_ffff00009077de54
                (s__SCP___s__tmp_dram_error_ffff0000907fb7ab,s_scp_crash_dump_ffff0000907eac7a);
      uVar6 = 0;
    }
  }
  return uVar6;
}


