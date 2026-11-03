
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000b8021c0c(undefined8 param_1,undefined4 param_2)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  undefined4 uVar6;
  uint uVar7;
  
  if (_DAT_ffff00001c800004 == 0) {
    FUN_ffff0000b804c33c(s__SCP__scp_region_info___0__skip_c_ffff0000b809646d);
    uVar6 = 0;
  }
  else {
    uVar3 = DAT_ffff0000b811b95c - DAT_ffff0000b811b958;
    iVar5 = FUN_ffff0000b804a5f8
                      (PTR_DAT_ffff0000b81086d8,s_SCP_reserved_ffff0000b8099c1b,uVar3 + 0x700000,
                       &DAT_ffff0000b811b940,0xc,_DAT_ffff00001c800004,0,0);
    lVar4 = DAT_ffff0000b811b940;
    if (iVar5 == 0) {
      FUN_ffff0000b804d070(DAT_ffff0000b811b940,0,0x700000);
      FUN_ffff0000b804cd40
                (DAT_ffff0000b811b940 + (ulong)DAT_ffff0000b811b948,0xffff00001c800000,
                 DAT_ffff0000b811b94c - DAT_ffff0000b811b948);
      uVar7 = _DAT_ffff00001cf2a000;
      lVar1 = DAT_ffff0000b811b940 + (ulong)DAT_ffff0000b811b94c;
      _DAT_ffff00001cf2a000 = _DAT_ffff00001cf2a000 & 0x7fffffff | 0x30000000;
      FUN_ffff0000b804cd40(lVar1,0xffff00001cf60000,0x1c800);
      _DAT_ffff00001cf2a000 = uVar7 | 0xb0000000;
      FUN_ffff0000b804cd40(lVar1 + 0x72000,0xffff00001cf60000,0x1f800);
      _DAT_ffff00001cf2a000 = uVar7;
      FUN_ffff0000b80216d0(lVar1 + 0x3c000,DAT_ffff0000b811b940 + (ulong)DAT_ffff0000b811b950);
      FUN_ffff0000b8021580
                (DAT_ffff0000b811b940 + (ulong)DAT_ffff0000b811b950,
                 DAT_ffff0000b811b940 + (ulong)DAT_ffff0000b811b954);
      FUN_ffff0000b80217f4(DAT_ffff0000b811b940 + (ulong)DAT_ffff0000b811b954);
      uVar7 = DAT_ffff0000b811b958;
      uVar2 = _DAT_ffff00001c800018;
      if ((int)_DAT_ffff00001c800018 < 1) {
        FUN_ffff0000b804c33c(s__SCP__ap_dram_size_<_0_ffff0000b80863e0);
      }
      else {
        FUN_ffff0000b804c33c(s__SCP__ap_dram_size__x_ffff0000b809f6f2,_DAT_ffff00001c800018);
        if (uVar3 <= uVar2) {
          uVar2 = uVar3;
        }
        FUN_ffff0000b804cd40
                  (DAT_ffff0000b811b940 + (ulong)DAT_ffff0000b811b958,lVar4 + 0x700000,uVar2);
        uVar7 = (uVar2 + 3 & 0xfffffffc) + uVar7;
      }
      FUN_ffff0000b804c33c(s__SCP__scp_dump_size_total___0x_x_ffff0000b8096497,uVar7,param_2);
      uVar6 = FUN_ffff0000b80219f0(lVar4,param_1,uVar7,param_2);
      FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,DAT_ffff0000b811b940);
    }
    else {
      FUN_ffff0000b804c33c
                (s__SCP___s__tmp_dram_error_ffff0000b809d78b,s_scp_crash_dump_ffff0000b8094549);
      uVar6 = 0;
    }
  }
  return uVar6;
}


