
/* RmcDcCommonReqHandler::getSupportNetworkTypes(int) */

void __thiscall
RmcDcCommonReqHandler::getSupportNetworkTypes(RmcDcCommonReqHandler *this,int param_1)

{
  long lVar1;
  undefined *puVar2;
  uint uVar3;
  ulong uVar4;
  long lVar5;
  long lVar6;
  uint uVar7;
  uint uVar8;
  undefined8 local_70;
  long local_68;
  
  puVar2 = PTR_s_LastApnTable_005d4298;
  lVar1 = tpidr_el0;
  local_68 = *(long *)(lVar1 + 0x28);
  uVar4 = (ulong)*(uint *)(this + 8);
  if (*(int *)(PTR_s_nLastReqNum_005d4288 + uVar4 * 4) < 1) {
    uVar8 = 0;
    uVar7 = 0;
  }
  else {
    lVar6 = 0;
    lVar5 = 0;
    uVar7 = 0;
    uVar8 = 0;
    do {
      uVar3 = *(uint *)(*(long *)(puVar2 + uVar4 * 8) + lVar6 + 0x4c);
      uVar4 = __rfx_is_gt_mode();
      if ((uVar4 & 1) == 0) {
        mtkLogI("RmcDcCommon","[%d][%s]apnTypeId=%d supportedTypesBitmask=%d",
                *(undefined4 *)(this + 8),"getSupportNetworkTypes",param_1,uVar3);
      }
      else {
        local_70 = 0;
        android::String8::format("%s%s",&local_70,&DAT_001fbebf,"RmcDcCommon");
        mtkLogI(local_70,"[%d][%s]apnTypeId=%d supportedTypesBitmask=%d",*(undefined4 *)(this + 8),
                "getSupportNetworkTypes",param_1,uVar3);
        android::String8::~String8((String8 *)&local_70);
      }
      if ((uVar3 & param_1) != 0) {
        uVar3 = *(uint *)(*(long *)(puVar2 + (ulong)*(uint *)(this + 8) * 8) + lVar6 + 0x50);
        uVar4 = __rfx_is_gt_mode();
        if ((uVar4 & 1) == 0) {
          mtkLogI("RmcDcCommon","[%d][%s] bearerBitmask=%d",*(undefined4 *)(this + 8),
                  "getSupportNetworkTypes",uVar3);
        }
        else {
          local_70 = 0;
          android::String8::format("%s%s",&local_70,&DAT_001fbebf,"RmcDcCommon");
          mtkLogI(local_70,"[%d][%s] bearerBitmask=%d",*(undefined4 *)(this + 8),
                  "getSupportNetworkTypes",uVar3);
          android::String8::~String8((String8 *)&local_70);
        }
        if (uVar3 == 0) {
          uVar3 = 3;
          goto LAB_005118b0;
        }
        uVar8 = uVar8 | (uVar3 & 0x20000) >> 0x11;
        uVar7 = uVar7 | (uVar3 & 0x2000) >> 0xd;
      }
      uVar4 = (ulong)*(uint *)(this + 8);
      lVar5 = lVar5 + 1;
      lVar6 = lVar6 + 0x78;
    } while (lVar5 < *(int *)(PTR_s_nLastReqNum_005d4288 + uVar4 * 4));
  }
  uVar4 = __rfx_is_gt_mode();
  if ((uVar4 & 1) == 0) {
    mtkLogD("RmcDcCommon","[%d][%s] bSupportWifiBearer=%d bSupportCellularBearer=%d",
            *(undefined4 *)(this + 8),"getSupportNetworkTypes",uVar8,uVar7);
  }
  else {
    local_70 = 0;
    android::String8::format("%s%s",&local_70,&DAT_001fbebf,"RmcDcCommon");
    mtkLogD(local_70,"[%d][%s] bSupportWifiBearer=%d bSupportCellularBearer=%d",
            *(undefined4 *)(this + 8),"getSupportNetworkTypes",uVar8,uVar7);
    android::String8::~String8((String8 *)&local_70);
  }
  uVar3 = 2;
  if ((uVar8 & uVar7) != 0) {
    uVar3 = 3;
  }
  if (uVar8 == 0) {
    uVar3 = uVar7;
  }
LAB_005118b0:
  if (*(long *)(lVar1 + 0x28) == local_68) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar3);
}


