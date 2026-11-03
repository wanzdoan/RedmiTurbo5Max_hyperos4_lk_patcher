
undefined4 FUN_ffff0000be00cf98(uint param_1,undefined8 *param_2,int *param_3,int *param_4)

{
  int iVar1;
  int iVar2;
  int *piVar3;
  long lVar4;
  undefined8 uVar5;
  undefined4 uVar6;
  
  piVar3 = (int *)FUN_ffff0000be05f1bc(0x2030);
  if (piVar3 == (int *)0x0) {
    FUN_ffff0000be063b40
              (s_kedump__malloc_failed_at__s_ffff0000be0c5f85,
               s_kedump_get_data_info_ffff0000be0c28c1);
    FUN_ffff0000be05fd98
              (s_kedump__malloc_failed_at__s_ffff0000be0c5f85,
               s_kedump_get_data_info_ffff0000be0c28c1);
    uVar6 = 0;
  }
  else {
    FUN_ffff0000be060ae0(piVar3,0,0x2030);
    lVar4 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
    if (lVar4 == 0) {
      FUN_ffff0000be063b40
                (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
      FUN_ffff0000be05fd98
                (s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf);
      uVar6 = 0xffffffed;
    }
    else {
      iVar2 = FUN_ffff0000be05e000(lVar4,piVar3,0,0x2030);
      if (iVar2 < 0x2030) {
        FUN_ffff0000be063b40(s_kedump__failed_to_read_iheader___ffff0000be0a9fd2,iVar2);
        FUN_ffff0000be05fd98(s_kedump__failed_to_read_iheader___ffff0000be0a9fd2,iVar2);
        uVar6 = 0xfffffffb;
      }
      else if ((*piVar3 == -0x511f2153) && (0x11 < (uint)piVar3[1])) {
        if (param_1 < 0x80) {
          if (((param_2 == (undefined8 *)0x0) || (param_3 == (int *)0x0)) || (param_4 == (int *)0x0)
             ) {
            FUN_ffff0000be063b40(s_kedump__invalid_argument_number__ffff0000be0bbb0c,param_1);
            FUN_ffff0000be05fd98(s_kedump__invalid_argument_number__ffff0000be0bbb0c,param_1);
            uVar6 = 0xfffffffd;
          }
          else if (piVar3[(ulong)param_1 * 0x10 + 0xd] == 0) {
            uVar6 = 0xfffffffc;
          }
          else {
            uVar5 = FUN_ffff0000be060d00(piVar3 + (ulong)param_1 * 0x10 + 0x14);
            iVar2 = piVar3[(ulong)param_1 * 0x10 + 0xe];
            iVar1 = piVar3[(ulong)param_1 * 0x10 + 0xf];
            uVar6 = 0;
            *param_2 = uVar5;
            *param_3 = iVar2;
            *param_4 = iVar1;
          }
        }
        else {
          FUN_ffff0000be063b40(s_kedump__invalid_index_number__d_ffff0000be0b2b1a,param_1);
          FUN_ffff0000be05fd98(s_kedump__invalid_index_number__d_ffff0000be0b2b1a,param_1);
          uVar6 = 0xfffffffe;
        }
      }
      else {
        uVar6 = 0xffffffff;
      }
      FUN_ffff0000be05def0(lVar4);
    }
    thunk_FUN_ffff0000be09937c(piVar3);
  }
  return uVar6;
}


