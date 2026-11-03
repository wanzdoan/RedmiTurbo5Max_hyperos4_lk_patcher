
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009074456c(undefined8 param_1,long param_2,long param_3)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  uint uVar7;
  ulong uVar8;
  ulong local_60 [4];
  
  iVar4 = FUN_ffff00009074362c();
  iVar5 = FUN_ffff0000907430d8();
  iVar6 = FUN_ffff0000907430ec();
  uVar8 = FUN_ffff000090743618();
  if ((uVar8 & 1) == 0) {
    iVar4 = 0;
  }
  else {
    _DAT_ffff000031bd000c = 1;
    iVar4 = FUN_ffff000090785d18
                      (param_1,s_tinysys_mmup_RV33_A_ffff0000907f1750,param_3 + iVar4,
                       (long)(iVar5 - iVar4));
    if (iVar4 < 1) {
      iVar4 = -1;
    }
    else {
      lVar1 = param_3 + 0x600000;
      uVar7 = FUN_ffff000090785d18
                        (param_1,s_tinysys_mmup_RV33_A_dram_ffff0000907dfc5b,lVar1,0x200000);
      if ((int)uVar7 < 1) {
        uVar8 = 0;
      }
      else {
        uVar8 = (ulong)uVar7;
        lVar2 = lVar1 + (ulong)(uVar7 + 0x3ff & 0xfffffc00);
        uVar3 = lVar2 + uVar8;
        if ((ulong)(param_3 + iVar6) < uVar3) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cee0
                    (s__VCP__backup_image_overflow__p_>_ffff0000907db018,uVar3,param_3 + iVar6);
        }
        FUN_ffff00009077eb20(lVar2,lVar1);
      }
      local_60[2] = 0;
      local_60[3] = 0;
      local_60[0] = 0;
      local_60[1] = 0;
      FUN_ffff000090707c70(param_3,(long)iVar6);
      FUN_ffff00009071ebf4
                (0xc2000413,7,(long)iVar6,param_2,0x2000,iVar4,param_2 + 0x600000,uVar8,local_60);
      iVar4 = (int)uVar8 + iVar4;
      if ((local_60[0] & 0x80000000) != 0) {
        iVar4 = 0;
      }
    }
  }
  return iVar4;
}


