// ffff0000907394f4  size=588  FUN_ffff0000907394f4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907394f4(undefined8 param_1,undefined4 param_2)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  undefined4 uVar6;
  uint uVar7;
  
  if (_DAT_ffff00001c800004 == 0) {
    FUN_ffff00009077e11c(s__SCP__scp_region_info___0__skip_c_ffff0000907ee2db);
    uVar6 = 0;
  }
  else {
    uVar3 = _DAT_ffff00009086debc - _DAT_ffff00009086deb8;
    iVar5 = FUN_ffff00009077ba64
                      (DAT_ffff000090839928,s_SCP_reserved_ffff0000907f48b4,uVar3 + 0x700000,
                       &DAT_ffff00009086dea0,0xc,_DAT_ffff00001c800004,0,0);
    lVar4 = _DAT_ffff00009086dea0;
    if (iVar5 == 0) {
      FUN_ffff00009077ee50(_DAT_ffff00009086dea0,0,0x700000);
      FUN_ffff00009077eb20
                (_DAT_ffff00009086dea0 + (ulong)_DAT_ffff00009086dea8,0xffff00001c800000,
                 _DAT_ffff00009086deac - _DAT_ffff00009086dea8);
      uVar7 = _DAT_ffff00001cf2a000;
      lVar1 = _DAT_ffff00009086dea0 + (ulong)_DAT_ffff00009086deac;
      _DAT_ffff00001cf2a000 = _DAT_ffff00001cf2a000 & 0x7fffffff | 0x30000000;
      FUN_ffff00009077eb20(lVar1,0xffff00001cf60000,0x1c800);
      _DAT_ffff00001cf2a000 = uVar7 | 0xb0000000;
      FUN_ffff00009077eb20(lVar1 + 0x72000,0xffff00001cf60000,0x1f800);
      _DAT_ffff00001cf2a000 = uVar7;
      FUN_ffff000090738fb8(lVar1 + 0x3c000,_DAT_ffff00009086dea0 + (ulong)_DAT_ffff00009086deb0);
      FUN_ffff000090738e68
                (_DAT_ffff00009086dea0 + (ulong)_DAT_ffff00009086deb0,
                 _DAT_ffff00009086dea0 + (ulong)_DAT_ffff00009086deb4);
      FUN_ffff0000907390dc(_DAT_ffff00009086dea0 + (ulong)_DAT_ffff00009086deb4);
      uVar7 = _DAT_ffff00009086deb8;
      uVar2 = _DAT_ffff00001c800018;
      if ((int)_DAT_ffff00001c800018 < 1) {
        FUN_ffff00009077e11c(s__SCP__ap_dram_size_<_0_ffff0000907d11e9);
      }
      else {
        FUN_ffff00009077e11c(s__SCP__ap_dram_size__x_ffff0000907ff16a,_DAT_ffff00001c800018);
        if (uVar3 <= uVar2) {
          uVar2 = uVar3;
        }
        FUN_ffff00009077eb20
                  (_DAT_ffff00009086dea0 + (ulong)_DAT_ffff00009086deb8,lVar4 + 0x700000,uVar2);
        uVar7 = (uVar2 + 3 & 0xfffffffc) + uVar7;
      }
      FUN_ffff00009077e11c(s__SCP__scp_dump_size_total___0x_x_ffff0000907ee305,uVar7,param_2);
      uVar6 = FUN_ffff0000907392d8(lVar4,param_1,uVar7,param_2);
      FUN_ffff00009077be30(DAT_ffff000090839928,_DAT_ffff00009086dea0);
    }
    else {
      FUN_ffff00009077e11c
                (s__SCP___s__tmp_dram_error_ffff0000907fb947,s_scp_crash_dump_ffff0000907eadfd);
      uVar6 = 0;
    }
  }
  return uVar6;
}


