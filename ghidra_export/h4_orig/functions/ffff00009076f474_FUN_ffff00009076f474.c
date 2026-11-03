// ffff00009076f474  size=352  FUN_ffff00009076f474

int FUN_ffff00009076f474(uint *param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  long lVar5;
  int iVar6;
  undefined4 *puVar7;
  int iVar8;
  int local_14;
  
  uVar1 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
  uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
  if (uVar1 == 0x2ff20112) {
    uVar1 = (param_1[3] & 0xff00ff00) >> 8 | (param_1[3] & 0xff00ff) << 8;
    uVar2 = (param_1[1] & 0xff00ff00) >> 8 | (param_1[1] & 0xff00ff) << 8;
    if ((uVar1 >> 0x10 | uVar1 << 0x10) == (uVar2 >> 0x10 | uVar2 << 0x10)) {
      puVar7 = (undefined4 *)FUN_ffff00009076f358(param_1,4);
      if (puVar7 == (undefined4 *)0x0) {
        local_14 = -3;
      }
      else {
        local_14 = 0;
        *puVar7 = 0x9000000;
        uVar2 = (param_1[1] & 0xff00ff00) >> 8 | (param_1[1] & 0xff00ff) << 8;
        uVar1 = (param_1[2] & 0xff00ff00) >> 8 | (param_1[2] & 0xff00ff) << 8;
        uVar3 = (param_1[8] & 0xff00ff00) >> 8 | (param_1[8] & 0xff00ff) << 8;
        uVar4 = (param_1[9] & 0xff00ff00) >> 8 | (param_1[9] & 0xff00ff) << 8;
        uVar1 = (uVar4 >> 0x10 | uVar4 << 0x10) + (uVar1 >> 0x10 | uVar1 << 0x10);
        FUN_ffff00009077ec38
                  ((long)param_1 + (long)(int)uVar1,
                   (long)param_1 +
                   (long)(int)((uVar2 >> 0x10 | uVar2 << 0x10) - (uVar3 >> 0x10 | uVar3 << 0x10)));
        uVar2 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
        param_1[3] = uVar2 >> 0x10 | uVar2 << 0x10;
        iVar8 = 0;
        do {
          while (iVar6 = FUN_ffff00009076cd48(param_1,iVar8,&local_14), iVar6 == 3) {
            uVar2 = (param_1[2] & 0xff00ff00) >> 8 | (param_1[2] & 0xff00ff) << 8;
            uVar3 = (param_1[8] & 0xff00ff00) >> 8 | (param_1[8] & 0xff00ff) << 8;
            lVar5 = (long)iVar8 + (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
            uVar2 = *(uint *)((long)param_1 + lVar5 + 8);
            uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
            uVar2 = (uVar3 >> 0x10 | uVar3 << 0x10) + (uVar2 >> 0x10 | uVar2 << 0x10);
            uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
            *(uint *)((long)param_1 + lVar5 + 8) = uVar2 >> 0x10 | uVar2 << 0x10;
            iVar8 = local_14;
          }
          iVar8 = local_14;
        } while (iVar6 != 9);
        if (-1 < local_14) {
          local_14 = 0;
          uVar2 = (param_1[8] & 0xff00ff00) >> 8 | (param_1[8] & 0xff00ff) << 8;
          uVar1 = (uVar2 >> 0x10 | uVar2 << 0x10) + uVar1;
          param_1[6] = 0x10000000;
          uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
          *param_1 = 0xedfe0dd0;
          param_1[1] = uVar1 >> 0x10 | uVar1 << 0x10;
        }
      }
    }
    else {
      local_14 = -7;
    }
  }
  else {
    local_14 = -7;
    if (uVar1 != 0xd00dfeed) {
      local_14 = -9;
    }
  }
  return local_14;
}


