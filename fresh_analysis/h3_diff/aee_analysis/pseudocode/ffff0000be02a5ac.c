
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000be02a5ac(undefined8 param_1,undefined4 param_2)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  undefined4 uVar6;
  uint uVar7;
  
  if (_DAT_ffff00001c800004 == 0) {
    FUN_ffff0000be05fd98(s__SCP__scp_region_info___0__skip_c_ffff0000be0b8a75);
    uVar6 = 0;
  }
  else {
    uVar3 = DAT_ffff0000be11817c - DAT_ffff0000be118178;
    iVar5 = FUN_ffff0000be05d9a8
                      (PTR_DAT_ffff0000be0f5570,s_SCP_reserved_ffff0000be0bd111,uVar3 + 0x700000,
                       &DAT_ffff0000be118160,0xc,_DAT_ffff00001c800004,0,0);
    lVar4 = DAT_ffff0000be118160;
    if (iVar5 == 0) {
      FUN_ffff0000be060ae0(DAT_ffff0000be118160,0,0x700000);
      FUN_ffff0000be0607b0
                (DAT_ffff0000be118160 + (ulong)DAT_ffff0000be118168,0xffff00001c800000,
                 DAT_ffff0000be11816c - DAT_ffff0000be118168);
      uVar7 = _DAT_ffff00001cf2a000;
      lVar1 = DAT_ffff0000be118160 + (ulong)DAT_ffff0000be11816c;
      _DAT_ffff00001cf2a000 = _DAT_ffff00001cf2a000 & 0x7fffffff | 0x30000000;
      FUN_ffff0000be0607b0(lVar1,0xffff00001cf60000,0x1c800);
      _DAT_ffff00001cf2a000 = uVar7 | 0xb0000000;
      FUN_ffff0000be0607b0(lVar1 + 0x72000,0xffff00001cf60000,0x1f800);
      _DAT_ffff00001cf2a000 = uVar7;
      FUN_ffff0000be02a070(lVar1 + 0x3c000,DAT_ffff0000be118160 + (ulong)DAT_ffff0000be118170);
      FUN_ffff0000be029f20
                (DAT_ffff0000be118160 + (ulong)DAT_ffff0000be118170,
                 DAT_ffff0000be118160 + (ulong)DAT_ffff0000be118174);
      FUN_ffff0000be02a194(DAT_ffff0000be118160 + (ulong)DAT_ffff0000be118174);
      uVar7 = DAT_ffff0000be118178;
      uVar2 = _DAT_ffff00001c800018;
      if ((int)_DAT_ffff00001c800018 < 1) {
        FUN_ffff0000be05fd98(s__SCP__ap_dram_size_<_0_ffff0000be0a47e6);
      }
      else {
        FUN_ffff0000be05fd98(s__SCP__ap_dram_size__x_ffff0000be0c4008,_DAT_ffff00001c800018);
        if (uVar3 <= uVar2) {
          uVar2 = uVar3;
        }
        FUN_ffff0000be0607b0
                  (DAT_ffff0000be118160 + (ulong)DAT_ffff0000be118178,lVar4 + 0x700000,uVar2);
        uVar7 = (uVar2 + 3 & 0xfffffffc) + uVar7;
      }
      FUN_ffff0000be05fd98(s__SCP__scp_dump_size_total___0x_x_ffff0000be0b8a9f,uVar7,param_2);
      uVar6 = FUN_ffff0000be02a390(lVar4,param_1,uVar7,param_2);
      FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,DAT_ffff0000be118160);
    }
    else {
      FUN_ffff0000be05fd98
                (s__SCP___s__tmp_dram_error_ffff0000be0c1bb3,s_scp_crash_dump_ffff0000be0b656c);
      uVar6 = 0;
    }
  }
  return uVar6;
}


