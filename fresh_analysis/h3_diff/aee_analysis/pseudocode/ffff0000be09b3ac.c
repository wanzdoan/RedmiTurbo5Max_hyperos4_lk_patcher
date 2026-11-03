
int FUN_ffff0000be09b3ac(void)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  int iVar4;
  long lVar5;
  long lVar6;
  byte bVar7;
  ulong uVar8;
  uint uVar9;
  byte local_58 [4];
  char local_54 [4];
  
  FUN_ffff0000be05fd98(s__s_IN_ffff0000be0acfc9,s_do_auth_token_check_ffff0000be0c5d1b);
  lVar5 = FUN_ffff0000be05f1bc(0x101);
  if (lVar5 == 0) {
    FUN_ffff0000be05fd98(s_Failed_to_malloc_token_buffer_ffff0000be0c39e6);
    iVar4 = -1;
  }
  else {
    local_54[0] = '\0';
    local_58[0] = 0;
    FUN_ffff0000be060ae0(lVar5,0,0x101);
    lVar6 = FUN_ffff0000be05f1bc(0x101);
    if (lVar6 == 0) {
      FUN_ffff0000be05fd98(s_Failed_to_malloc_auth_token_buff_ffff0000be0a405e);
      thunk_FUN_ffff0000be09937c(lVar5);
      iVar4 = -1;
    }
    else {
      FUN_ffff0000be060ae0(lVar6,0,0x101);
      iVar4 = FUN_ffff0000be0a254c(0x18,lVar5,0x100);
      if (iVar4 == 0) {
        iVar3 = *(int *)(lVar5 + 8);
        iVar4 = FUN_ffff0000be0a254c(0x1c,lVar6,0x100);
        if (iVar4 == 0) {
          uVar1 = *(int *)(lVar6 + 0xc) + 0x10;
          if (uVar1 < 0x11) {
            uVar9 = 0x10;
            bVar7 = 0;
          }
          else {
            uVar8 = 0x10;
            do {
              if ((ulong)uVar1 - 2 < uVar8) {
                iVar4 = -0xfffffe;
                FUN_ffff0000be05fd98
                          (s_Auth_token_length_error__offset__ffff0000be0b1881,uVar8,uVar1);
                goto LAB_ffff0000be09b5a4;
              }
              FUN_ffff0000be0607b0(local_54,lVar6 + uVar8,1);
              FUN_ffff0000be05fd98(s_Type_value___02x_ffff0000be0a6722,local_54[0]);
              FUN_ffff0000be0607b0(local_58,lVar6 + (ulong)((int)uVar8 + 1),1);
              FUN_ffff0000be05fd98(s_Len_value___02x_ffff0000be0c4bb5,local_58[0]);
              uVar9 = (int)uVar8 + 2;
              uVar2 = uVar9 + local_58[0];
              uVar8 = (ulong)uVar2;
              if (uVar1 < uVar2) {
                iVar4 = -0xfffffe;
                FUN_ffff0000be05fd98
                          (s_Length_error__invalid_length__cu_ffff0000be0af2f5,(uint)local_58[0],
                           uVar9,uVar1);
                goto LAB_ffff0000be09b5a4;
              }
              bVar7 = local_58[0];
            } while ((local_54[0] != '\v') && (uVar9 = uVar2, uVar2 < uVar1));
          }
          FUN_ffff0000be05fd98(s_Get_the_offset___d__len___d_ffff0000be0b4db0,uVar9,bVar7);
          iVar4 = FUN_ffff0000be0a2208(lVar5,iVar3 + 0xc,lVar6 + (ulong)uVar9,local_58[0]);
          if (iVar4 == 0) {
            iVar4 = 0;
          }
          else {
            FUN_ffff0000be05fd98(s_Error___ecc_verify_fail_ffff0000be0b83dc);
            iVar4 = 8;
          }
          FUN_ffff0000be05fd98(s__s_OUT_ffff0000be0c1274,s_do_auth_token_check_ffff0000be0c5d1b);
        }
        else {
          FUN_ffff0000be05fd98(s_Read_authtoken_Failed__ffff0000be0a4082);
        }
      }
      else {
        FUN_ffff0000be05fd98(s_Read_token_Failed__ffff0000be0c5d2f);
      }
LAB_ffff0000be09b5a4:
      thunk_FUN_ffff0000be09937c(lVar5);
      thunk_FUN_ffff0000be09937c(lVar6);
    }
  }
  return iVar4;
}


