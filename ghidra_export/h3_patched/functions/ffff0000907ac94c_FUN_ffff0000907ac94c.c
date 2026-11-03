// ffff0000907ac94c  size=428  FUN_ffff0000907ac94c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907ac94c(uint param_1,uint param_2)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  uint *puVar4;
  ulong uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  uint local_58;
  uint local_54;
  
  local_54 = 0;
  uVar3 = FUN_ffff0000907ab798();
  if ((int)uVar3 != 0) {
    return uVar3;
  }
  if (DAT_ffff00009090af30 != '\x01') {
    return 0x10400c;
  }
  uVar5 = (ulong)_DAT_ffff00009090af34;
  if (_DAT_ffff00009090af34 != 0) {
    puVar4 = &DAT_ffff00009083754c;
    do {
      if (puVar4[-3] == param_1) {
        if (puVar4[-2] == 0) {
          return 0;
        }
        uVar6 = puVar4[-1];
        uVar1 = *puVar4;
        if (uVar1 < uVar6) {
          return 0x104008;
        }
        uVar7 = puVar4[2];
        if (param_2 <= puVar4[2]) {
          uVar7 = param_2;
        }
        do {
          uVar8 = 0xffffffff;
          if (uVar7 < 0x20) {
            uVar8 = ~(-1 << (ulong)(uVar7 & 0x1f));
          }
          local_58 = 0;
          iVar2 = FUN_ffff0000907acc50(uVar6,&local_54);
          if (iVar2 != 0) {
            return 0x104004;
          }
          if (local_54 == uVar8) {
            iVar2 = FUN_ffff0000907ad53c();
            if (iVar2 != 0) {
              FUN_ffff00009077de54(s_group__x_bypass_version_update_ffff0000907f39a1,param_1);
            }
          }
          else {
            iVar2 = FUN_ffff0000907acc24(uVar6);
            if (iVar2 != 0) {
              return 0x104005;
            }
            iVar2 = FUN_ffff0000907acc50(uVar6,&local_58);
            if (iVar2 != 0) {
              return 0x104004;
            }
            if (uVar8 != local_58) {
              iVar2 = FUN_ffff0000907ad53c();
              if (iVar2 == 0) {
                return 0x104005;
              }
              FUN_ffff00009077de54
                        (s___s__write_0x_x__read_0x_x_ffff0000907d063f,&DAT_ffff0000907f20e7,uVar8,
                         local_58);
              return 0x104005;
            }
          }
          if (0xffffffdf < uVar7 - 0x20) {
            return 0;
          }
          uVar6 = uVar6 + 1;
          uVar7 = uVar7 - 0x20;
        } while (uVar6 <= uVar1);
        return 0;
      }
      puVar4 = puVar4 + 7;
      uVar5 = uVar5 - 1;
    } while (uVar5 != 0);
  }
  return 0x104009;
}


