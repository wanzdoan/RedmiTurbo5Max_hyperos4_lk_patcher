
/* responseCdmaInformationRecords(android::Parcel&, void*, unsigned long) */

void responseCdmaInformationRecords(Parcel *param_1,void *param_2,ulong param_3)

{
  int iVar1;
  byte bVar2;
  long lVar3;
  int iVar4;
  ulong uVar5;
  void *pvVar6;
  void *pvVar7;
  undefined8 uVar8;
  char *pcVar9;
  uint uVar10;
  ulong uVar11;
  undefined8 local_50;
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  if ((param_2 == (void *)0x0) && (param_3 != 0)) {
    uVar5 = __rfx_is_gt_mode();
    if ((uVar5 & 1) == 0) {
      pcVar9 = "invalid response: NULL";
LAB_00398c8c:
      mtkLogE("ParcelUtil",pcVar9);
      uVar8 = 0xffffffff;
      goto LAB_00398d60;
    }
    local_50 = 0;
    android::String8::format("%s%s",&local_50,&DAT_001fbebf,"ParcelUtil");
    pcVar9 = "invalid response: NULL";
LAB_0039896c:
    mtkLogE(local_50,pcVar9);
  }
  else {
    if (param_3 == 0x3c4) {
      bVar2 = *(byte *)param_2;
      uVar10 = (uint)bVar2;
      if (9 < uVar10) {
        uVar10 = 10;
      }
      uVar5 = (ulong)uVar10;
      iVar4 = (int)param_1;
      android::Parcel::writeInt32(iVar4);
      if (bVar2 != 0) {
        do {
          android::Parcel::writeInt32(iVar4);
          iVar1 = *(int *)((long)param_2 + 4);
          if (iVar1 < 5) {
            if (iVar1 - 1U < 3) {
              bVar2 = *(byte *)((long)param_2 + 8);
              uVar11 = (ulong)bVar2;
              if (0x51 < bVar2) {
LAB_00398d04:
                uVar5 = __rfx_is_gt_mode();
                if ((uVar5 & 1) == 0) {
                  uVar5 = (ulong)*(byte *)((long)param_2 + 8);
                  pcVar9 = 
                  "invalid display info response length %d                           expected not more than %d\n"
                  ;
                  uVar8 = 0x51;
                  goto LAB_00398cb0;
                }
                local_50 = 0;
                android::String8::format("%s%s",&local_50,&DAT_001fbebf,"ParcelUtil");
                pcVar9 = 
                "invalid display info response length %d                           expected not more than %d\n"
                ;
                uVar5 = (ulong)*(byte *)((long)param_2 + 8);
                uVar8 = 0x51;
                goto LAB_00398c68;
              }
              pvVar6 = calloc(uVar11 + 1,1);
              if (pvVar6 != (void *)0x0) {
                if (bVar2 == 0) {
                  uVar11 = 0;
                }
                else {
                  memcpy(pvVar6,(void *)((long)param_2 + 9),uVar11);
                }
                *(undefined1 *)((long)pvVar6 + uVar11) = 0;
                local_50 = 0;
                pvVar7 = (void *)strdup8to16(pvVar6,&local_50);
                android::Parcel::writeString16((wchar16 *)param_1,(ulong)pvVar7);
                free(pvVar7);
                free(pvVar6);
                android::Parcel::writeInt32(iVar4);
                android::Parcel::writeInt32(iVar4);
                android::Parcel::writeInt32(iVar4);
                goto LAB_003989d8;
              }
LAB_00398cbc:
              uVar5 = __rfx_is_gt_mode();
              if ((uVar5 & 1) == 0) {
                mtkLogE("ParcelUtil","Memory allocation failed for responseCdmaInformationRecords");
              }
              else {
                local_50 = 0;
                android::String8::format("%s%s",&local_50,&DAT_001fbebf,"ParcelUtil");
                mtkLogE(local_50,"Memory allocation failed for responseCdmaInformationRecords");
                android::String8::~String8((String8 *)&local_50);
              }
              uVar8 = 0xfffffff4;
              goto LAB_00398d60;
            }
            if (iVar1 != 0) {
              if (iVar1 != 4) goto LAB_00398e2c;
LAB_003989b0:
              android::Parcel::writeInt32(iVar4);
              android::Parcel::writeInt32(iVar4);
              android::Parcel::writeInt32(iVar4);
              goto LAB_003989d8;
            }
LAB_00398a24:
            bVar2 = *(byte *)((long)param_2 + 8);
            uVar11 = (ulong)bVar2;
            if (0x40 < bVar2) {
              uVar5 = __rfx_is_gt_mode();
              if ((uVar5 & 1) == 0) {
                uVar5 = (ulong)*(byte *)((long)param_2 + 8);
                pcVar9 = 
                "invalid display info response length %d                           expected not more than %d\n"
                ;
                uVar8 = 0x40;
                goto LAB_00398cb0;
              }
              local_50 = 0;
              android::String8::format("%s%s",&local_50,&DAT_001fbebf,"ParcelUtil");
              pcVar9 = 
              "invalid display info response length %d                           expected not more than %d\n"
              ;
              uVar5 = (ulong)*(byte *)((long)param_2 + 8);
              uVar8 = 0x40;
              goto LAB_00398c68;
            }
            pvVar6 = calloc(uVar11 + 1,1);
            if (pvVar6 == (void *)0x0) goto LAB_00398cbc;
            if (bVar2 == 0) {
              uVar11 = 0;
            }
            else {
              memcpy(pvVar6,(void *)((long)param_2 + 9),uVar11);
            }
            *(undefined1 *)((long)pvVar6 + uVar11) = 0;
            local_50 = 0;
            pvVar7 = (void *)strdup8to16(pvVar6,&local_50);
            android::Parcel::writeString16((wchar16 *)param_1,(ulong)pvVar7);
            free(pvVar7);
            free(pvVar6);
          }
          else {
            if (iVar1 < 8) {
              if (iVar1 != 5) {
                if (iVar1 == 6) goto LAB_003989b0;
                if (iVar1 == 7) goto LAB_00398a24;
LAB_00398e2c:
                uVar5 = __rfx_is_gt_mode();
                if ((uVar5 & 1) == 0) {
                  pcVar9 = "Incorrect name value";
                  goto LAB_00398c8c;
                }
                local_50 = 0;
                android::String8::format("%s%s",&local_50,&DAT_001fbebf,"ParcelUtil");
                pcVar9 = "Incorrect name value";
                goto LAB_0039896c;
              }
              bVar2 = *(byte *)((long)param_2 + 8);
              uVar11 = (ulong)bVar2;
              if (0x51 < bVar2) goto LAB_00398d04;
              pvVar6 = calloc(uVar11 + 1,1);
              if (pvVar6 == (void *)0x0) goto LAB_00398cbc;
              if (bVar2 == 0) {
                uVar11 = 0;
              }
              else {
                memcpy(pvVar6,(void *)((long)param_2 + 9),uVar11);
              }
              *(undefined1 *)((long)pvVar6 + uVar11) = 0;
              local_50 = 0;
              pvVar7 = (void *)strdup8to16(pvVar6,&local_50);
              android::Parcel::writeString16((wchar16 *)param_1,(ulong)pvVar7);
              free(pvVar7);
              free(pvVar6);
              android::Parcel::writeInt32(iVar4);
              android::Parcel::writeInt32(iVar4);
              android::Parcel::writeInt32(iVar4);
              android::Parcel::writeInt32(iVar4);
            }
            else if (iVar1 != 8) {
              if (iVar1 != 10) {
                if (iVar1 != 9) goto LAB_00398e2c;
                uVar5 = __rfx_is_gt_mode();
                if ((uVar5 & 1) == 0) {
                  pcVar9 = "RIL_CDMA_T53_RELEASE_INFO_REC: return INVALID_RESPONSE";
                  goto LAB_00398c8c;
                }
                local_50 = 0;
                android::String8::format("%s%s",&local_50,&DAT_001fbebf,"ParcelUtil");
                pcVar9 = "RIL_CDMA_T53_RELEASE_INFO_REC: return INVALID_RESPONSE";
                goto LAB_0039896c;
              }
              android::Parcel::writeInt32(iVar4);
            }
LAB_003989d8:
            android::Parcel::writeInt32(iVar4);
          }
          uVar5 = uVar5 - 1;
          param_2 = (void *)((long)param_2 + 0x60);
        } while (uVar5 != 0);
      }
      uVar8 = 0;
      goto LAB_00398d60;
    }
    uVar5 = __rfx_is_gt_mode();
    if ((uVar5 & 1) == 0) {
      pcVar9 = 
      "responseCdmaInformationRecords: invalid response length %d expected multiple of %d\n";
      uVar5 = param_3 & 0xffffffff;
      uVar8 = 8;
LAB_00398cb0:
      mtkLogE("ParcelUtil",pcVar9,uVar5,uVar8);
      uVar8 = 0xffffffff;
      goto LAB_00398d60;
    }
    local_50 = 0;
    android::String8::format("%s%s",&local_50,&DAT_001fbebf,"ParcelUtil");
    pcVar9 = "responseCdmaInformationRecords: invalid response length %d expected multiple of %d\n";
    uVar5 = param_3 & 0xffffffff;
    uVar8 = 8;
LAB_00398c68:
    mtkLogE(local_50,pcVar9,uVar5,uVar8);
  }
  android::String8::~String8((String8 *)&local_50);
  uVar8 = 0xffffffff;
LAB_00398d60:
  if (*(long *)(lVar3 + 0x28) != local_48) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail(uVar8);
  }
  return;
}


