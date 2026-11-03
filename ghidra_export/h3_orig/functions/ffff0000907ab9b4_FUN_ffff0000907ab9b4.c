// ffff0000907ab9b4  size=380  FUN_ffff0000907ab9b4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000907ab9b4(uint param_1,int *param_2)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  ulong uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint local_34;
  
  local_34 = 0;
  if (param_2 == (int *)0x0) {
    uVar7 = 6;
  }
  else {
    if (DAT_ffff00009090af30 != '\x01') {
      uVar5 = 0;
      uVar6 = 0;
      uVar7 = 0x10400c;
      goto LAB_ffff0000907aba4c;
    }
    uVar4 = (ulong)_DAT_ffff00009090af34;
    if (_DAT_ffff00009090af34 != 0) {
      puVar3 = &DAT_ffff000090837548;
      do {
        if (puVar3[-2] == param_1) {
          if (puVar3[-1] == 0) {
            uVar7 = 0;
            goto LAB_ffff0000907aba78;
          }
          uVar5 = *puVar3;
          uVar6 = puVar3[1];
          if (uVar6 < uVar5) {
            uVar7 = 0x104008;
          }
          else {
            *param_2 = 0;
            uVar7 = uVar5;
            while (iVar2 = FUN_ffff0000907acc50(uVar7,&local_34), iVar2 == 0) {
              uVar7 = uVar7 + 1;
              uVar1 = local_34 - (local_34 >> 1 & 0x55555555);
              uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
              *param_2 = *param_2 + ((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
              if (uVar6 < uVar7) {
                return 0;
              }
            }
            uVar7 = 0x104004;
          }
          goto LAB_ffff0000907aba4c;
        }
        puVar3 = puVar3 + 7;
        uVar4 = uVar4 - 1;
      } while (uVar4 != 0);
    }
    uVar7 = 0x4009;
  }
  uVar5 = 0;
  uVar6 = 0;
  uVar7 = uVar7 | 0x100000;
LAB_ffff0000907aba4c:
  iVar2 = FUN_ffff0000907ad53c();
  if (iVar2 != 0) {
    FUN_ffff00009077de54
              (s___s__fail_to_get_otp_ver__start__ffff0000907f08cc,&DAT_ffff0000907f20fe,uVar5,uVar6
               ,uVar7);
  }
  if (param_2 != (int *)0x0) {
LAB_ffff0000907aba78:
    *param_2 = 0;
  }
  return uVar7;
}


