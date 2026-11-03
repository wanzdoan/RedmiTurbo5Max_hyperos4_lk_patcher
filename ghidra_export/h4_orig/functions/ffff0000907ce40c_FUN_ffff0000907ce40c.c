// ffff0000907ce40c  size=624  FUN_ffff0000907ce40c

int FUN_ffff0000907ce40c(void)

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
  
  FUN_ffff00009077e11c(s__s_IN_ffff0000907dd40e,s_do_auth_token_check_ffff000090801cd2);
  lVar5 = FUN_ffff00009077d514(0x101);
  if (lVar5 == 0) {
    FUN_ffff00009077e11c(s_Failed_to_malloc_token_buffer_ffff0000907fe857);
    iVar4 = -1;
  }
  else {
    local_54[0] = '\0';
    local_58[0] = 0;
    FUN_ffff00009077ee50(lVar5,0,0x101);
    lVar6 = FUN_ffff00009077d514(0x101);
    if (lVar6 == 0) {
      FUN_ffff00009077e11c(s_Failed_to_malloc_auth_token_buff_ffff0000907d0850);
      thunk_FUN_ffff0000907c314c(lVar5);
      iVar4 = -1;
    }
    else {
      FUN_ffff00009077ee50(lVar6,0,0x101);
      iVar4 = FUN_ffff0000907cd89c(0x18,lVar5,0x100);
      if (iVar4 == 0) {
        iVar3 = *(int *)(lVar5 + 8);
        iVar4 = FUN_ffff0000907cd89c(0x1c,lVar6,0x100);
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
                FUN_ffff00009077e11c
                          (s_Auth_token_length_error__offset__ffff0000907e3fd7,uVar8,uVar1);
                goto LAB_ffff0000907ce604;
              }
              FUN_ffff00009077eb20(local_54,lVar6 + uVar8,1);
              FUN_ffff00009077e11c(s_Type_value___02x_ffff0000907d3cab,local_54[0]);
              FUN_ffff00009077eb20(local_58,lVar6 + (ulong)((int)uVar8 + 1),1);
              FUN_ffff00009077e11c(s_Len_value___02x_ffff0000908002a1,local_58[0]);
              uVar9 = (int)uVar8 + 2;
              uVar2 = uVar9 + local_58[0];
              uVar8 = (ulong)uVar2;
              if (uVar1 < uVar2) {
                iVar4 = -0xfffffe;
                FUN_ffff00009077e11c
                          (s_Length_error__invalid_length__cu_ffff0000907e08fa,(uint)local_58[0],
                           uVar9,uVar1);
                goto LAB_ffff0000907ce604;
              }
              bVar7 = local_58[0];
            } while ((local_54[0] != '\v') && (uVar9 = uVar2, uVar2 < uVar1));
          }
          FUN_ffff00009077e11c(s_Get_the_offset___d__len___d_ffff0000907e8b40,uVar9,bVar7);
          iVar4 = FUN_ffff0000907cd558(lVar5,iVar3 + 0xc,lVar6 + (ulong)uVar9,local_58[0]);
          if (iVar4 == 0) {
            iVar4 = 0;
          }
          else {
            FUN_ffff00009077e11c(s_Error___ecc_verify_fail_ffff0000907ed318);
            iVar4 = 8;
          }
          FUN_ffff00009077e11c(s__s_OUT_ffff0000907fa94e,s_do_auth_token_check_ffff000090801cd2);
        }
        else {
          FUN_ffff00009077e11c(s_Read_authtoken_Failed__ffff0000907d0874);
        }
      }
      else {
        FUN_ffff00009077e11c(s_Read_token_Failed__ffff000090801ce6);
      }
LAB_ffff0000907ce604:
      thunk_FUN_ffff0000907c314c(lVar5);
      thunk_FUN_ffff0000907c314c(lVar6);
    }
  }
  return iVar4;
}


