
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff000090743640(undefined8 param_1,long param_2,long param_3)

{
  long lVar1;
  long lVar2;
  ulong uVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  uint uVar7;
  int iVar8;
  int iVar9;
  int iVar10;
  ulong uVar11;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  iVar4 = FUN_ffff0000907430d8();
  iVar5 = FUN_ffff0000907430ec();
  iVar6 = FUN_ffff00009074362c();
  _DAT_ffff000031bd0004 = 1;
  FUN_ffff00009077ee50(0xffff000031800000,0,(long)iVar4);
  iVar4 = FUN_ffff000090785d18(param_1,s_tinysys_vcp_RV55_A_ffff0000907e30bd,param_3,(long)iVar4);
  if (iVar4 < 1) {
    uVar7 = 0xffffffff;
  }
  else {
    lVar1 = param_3 + 0x200000;
    uVar7 = FUN_ffff000090785d18(param_1,s_tinysys_vcp_RV55_A_dram_ffff0000907fd7c5,lVar1,0x200000);
    if ((int)uVar7 < 1) {
      uVar11 = 0;
    }
    else {
      uVar11 = (ulong)uVar7;
      lVar2 = lVar1 + (ulong)(uVar7 + 0x3ff & 0xfffffc00);
      uVar3 = lVar2 + uVar11;
      if ((ulong)(param_3 + iVar5) < uVar3) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cee0
                  (s__VCP__backup_image_overflow__p_>_ffff0000907db018,uVar3,param_3 + iVar5);
      }
      FUN_ffff00009077eb20(lVar2,lVar1);
    }
    local_60 = 0;
    uStack_58 = 0;
    local_70 = 0;
    uStack_68 = 0;
    FUN_ffff000090707c70(param_3,(long)iVar5);
    FUN_ffff00009071ebf4(0xc2000413,6,(long)iVar6,0,0,0,0,0,&local_70);
    FUN_ffff00009071ebf4
              (0xc2000413,2,(long)iVar5,param_2,0x2000,iVar4,param_2 + 0x200000,uVar11,&local_70);
    iVar5 = (int)local_70;
    FUN_ffff00009074456c(param_1,param_2,param_3);
    DataSynchronizationBarrier(3,3,0);
    iVar6 = FUN_ffff000090743100();
    iVar8 = FUN_ffff00009074313c();
    iVar9 = FUN_ffff000090743128();
    iVar10 = FUN_ffff000090743150();
    FUN_ffff00009071ebf4
              (0xc2000413,0,(long)iVar6,(long)iVar8,(long)iVar9,(long)iVar10,0,0,&local_70);
    uVar7 = (int)uVar11 + iVar4 & (iVar5 >> 0x1f ^ 0xffffffffU);
  }
  return uVar7;
}


